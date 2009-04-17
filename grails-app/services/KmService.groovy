import weka.core.Statistics;

class KmService {

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
    
    
/*

    //compute the p-value between two sample series
    public Double getLogRankPValue(Collection<KMSampleDTO> group1, Collection<KMSampleDTO> group2) {
        //need to
        ArrayList<KMSampleDTO> samples = new ArrayList<KMSampleDTO>();
        samples.addAll(group1);
        samples.addAll(group2);
        Collections.sort(samples, new KaplanMeierSampleComparator());
        float u = 0;
        float v = 0;
        float a = 0;
        float b = 0;
        float c = (float) group1.size();
        float d = (float) group2.size();
        float t = 0;
        for (int i = 0; i < samples.size(); i++) {
            KMSampleDTO event = samples.get(i);
            if (event.getSurvivalLength() > t) {
                if (a+b > 0) {
                    u += a - (a + b) * (a + c) / (a + b + c + d);
                    v += (a + b)
                            * (c + d)
                            * (a + c)
                            * (b + d)
                            / ((a + b + c + d - 1) *
                                    (Math.pow((a + b + c + d), 2)));
                }
                a = 0;
                b = 0;
                t = event.getSurvivalLength();
            }
            if (group1.contains(event)) {
                if (event.getCensor()) {
                    a += 1;
                }
                c-=1;
            } else {
                if (event.getCensor()) {
                    b+=1;
                }
                d-=1;
            }
        }

        if ( (a > 0 | b > 0) & (a+b+c+d-1>0) ) {
            u += a - (a + b) * (a + c) / (a + b + c + d);
            v += (a + b)
                    * (c + d)
                    * (a + c)
                    * (b + d)
                    / ((a + b + c + d - 1) *
                            (Math.pow((a + b + c + d), 2)));
        }

        if (v > 0) {
            return new Double(Statistics.chiSquaredProbability(Math.pow(u, 2.0) / v, 1.0));
        } else {
            return new Double(-100.0);
        }
    }
*/
}
