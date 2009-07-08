import weka.core.Statistics;
import grails.converters.*
import org.json.simple.*

class KmService {
	def savedAnalysisService
	def idService

    def plotCoordinates(sampleCollection) {
	
		def samples = sampleCollection.sort { one, two ->
			int val;
            float i1 = one["survival"];
            float i2 = two["survival"];
            if (i1 > i2) {
                val = 1;
            } else if (i1 == i2) {
                val = 0;
            } else {
                val = -1;
            }
            return val;
		}
        float surv = 1;
        float prevSurvTime = 0;
        float curSurvTime = 0;
        int d = 0;
        int r = samples.size();
        int left = samples.size();
        def points = [];
        for (int i = 0; i < samples.size(); i++) {
            curSurvTime = samples.get(i)["survival"];
            if (curSurvTime > prevSurvTime) {
                if (d>0) {
					def point = [:]
                    points << [x: new Float(prevSurvTime), y: new Float(surv), census: false];
                    surv = surv * (r-d)/r;
					points << [x: new Float(prevSurvTime), y: new Float(surv), census: false];
                } else {
					points << [x: new Float(prevSurvTime), y: new Float(surv), census: true];
                }
                prevSurvTime = curSurvTime;
                d = 0;
                r = left;
            }
            if (samples.get(i)["censor"]) {
                d++;
            }
            left--;
        }
        if (d>0) {
			points << [x: new Float(prevSurvTime), y: new Float(surv), census: false];
            surv = surv * (r-d)/r;
			points << [x: new Float(prevSurvTime), y: new Float(surv), census: false];
        } else {
            points << [x: new Float(prevSurvTime), y: new Float(surv), census: true];
        }
        return points;
    }
    
    

    //compute the p-value between two sample series
    def getLogRankPValue(group1, group2) {
        //need to
        def samples = [];
        samples.addAll(group1);
        samples.addAll(group2);
		samples = samples.sort { one, two ->
			int val;
            float i1 = one["survival"];
            float i2 = two["survival"];
            if (i1 > i2) {
                val = 1;
            } else if (i1 == i2) {
                val = 0;
            } else {
                val = -1;
            }
            return val;
		}
        float u = 0;
        float v = 0;
        float a = 0;
        float b = 0;
        float c = (float) group1.size();
        float d = (float) group2.size();
        float t = 0;
		samples.each { event ->
			if (event["survival"] > t) {
                if (a+b > 0) {
                    u += a - (a + b) * (a + c) / (a + b + c + d);
                    v += (a + b) * (c + d) * (a + c) * (b + d)  / ((a + b + c + d - 1) * (Math.pow((a + b + c + d), 2)));
                }
                a = 0;
                b = 0;
                t = event["survival"];
            }
            if (group1.contains(event)) {
                if (event["censor"]) {
                    a += 1;
                }
                c-=1;
            } else {
                if (event["censor"]) {
                    b+=1;
                }
                d-=1;
            }			
		}

        if ( (a > 0 | b > 0) & (a+b+c+d-1>0) ) {
            u += a - (a + b) * (a + c) / (a + b + c + d);
            v += (a + b)  * (c + d) * (a + c) * (b + d) / ((a + b + c + d - 1) * (Math.pow((a + b + c + d), 2)));
        }

        if (v > 0) {
            return new Double(Statistics.chiSquaredProbability(Math.pow(u, 2.0) / v, 1.0));
        } else {
            return new Double(-100.0);
        }
    }
	
	//return highest mean expression for an analysis. 
	/****/
	def findReportersMeanExpression(analysisId){
		def geAnalysis = savedAnalysisService.getSavedAnalysis(analysisId)
		def sortedMeanExpression = new JSONArray()
		def comparator= [ compare:
		  {a,b-> b.equals(a)? 0: b<a? -1: 1 }
		] as Comparator
		
		
		def reporterExpressionValues = new TreeMap(comparator)
		geAnalysis.analysis.item.dataVectors.each { data ->
			def values = 0
			data.dataPoints.each { sample ->
			values += sample.x
			}
			if(values){
				println "$values / " + data.dataPoints.size()
		  		reporterExpressionValues[values/data.dataPoints.size()] = data.name
			}
		}
		
		reporterExpressionValues.each{ exp, reporter ->
			JSONObject values=new JSONObject();
			values.put("reporter",reporter)
			values.put("expression", exp)
			sortedMeanExpression.add(values)
		}
		if(sortedMeanExpression){
			sortedMeanExpression.each{
			//	println it["reporter"] + ":" + it["expression"]
			}
		}
		return sortedMeanExpression
	}
	
	//calculate fold change groupings in an analysis for a given reporter mean expression value
	def calculateFoldChangeGroupings(reporter, meanExpression, foldChange, analysisId){
		def geAnalysis = savedAnalysisService.getSavedAnalysis(analysisId)
		def groups = [:]
		def greaterThanFold = []
		def lessThanFold = []
		def inBetween = []
		
		println "ALL VALUES:"
		geAnalysis.analysis.item.dataVectors.each { data ->
			if(data.name.equals(reporter)){
			data.dataPoints.each { sample ->
			 
		//	println sample.id + ": " + sample.x
			def sampleFoldChange = sample.x - meanExpression
			    if((sampleFoldChange > (foldChange*-1)) && 
					(sampleFoldChange < foldChange)){
						inBetween << sample.id
				}
				else if(sampleFoldChange >= foldChange){
					greaterThanFold << sample.id
				}else if(sampleFoldChange < (foldChange*-1)){
					lessThanFold << sample.id
				}
			}
			}
		}
		if(greaterThanFold)
		groups['greater'] = idService.gdocIdsForSampleNames(greaterThanFold)
		if(lessThanFold)
		groups['less'] = idService.gdocIdsForSampleNames(lessThanFold)
		if(inBetween)
		groups['between'] = idService.gdocIdsForSampleNames(inBetween)
		//println "greater: " + groups['greater'].size()
		//println "less: " + groups['less'].size()
		//println "in between" + groups['between'].size()
		return groups
	}

}
