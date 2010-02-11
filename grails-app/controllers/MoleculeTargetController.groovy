import org.compass.core.engine.SearchEngineQueryParseException
import grails.converters.*

class MoleculeTargetController {
	def drugDiscoveryService
	def annotationService
	def searchableService
	
	def index = {
		println params
		def ligands
		if(chainModel){
			ligands = chainModel["ligands"]
			println ligands.class
		}
		[ligands:ligands]
	}
	
	def searchLigands = { MoleculeTargetCommand cmd ->
		println "Entity: " + cmd.entity
		println cmd.errors
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index',params:params)
			return
		}else{
			def targets = []
			if(cmd.entity || cmd.molWeightLow || cmd.molWeightHigh || cmd.affinity){
				
				if(!cmd.entity && (cmd.molWeightLow || cmd.molWeightHigh)){
					if(!cmd.molWeightLow){
						cmd.molWeightLow = 0
					}
					if(!cmd.molWeightHigh){
						cmd.molWeightHigh = 99999
					}
					targets = Molecule.search(){
						must(between("weight",cmd.molWeightLow.toDouble(),cmd.molWeightHigh.toDouble(),true))
					}
					chain(action:index,model:[ligands:targets],params:[molWeightLow:params.molWeightLow,molWeightHigh:params.molWeightHigh,entityName:params.entity])
					return
				}
				try { 
				def searchTerm = cmd.entity
					//is it a gene symbol? if so, build the search term
					def alias = annotationService.findGeneByAlias(searchTerm)
					if(alias){
						println "its a gene symbol"
						def proteinNames = []
						def queryStringToCall = ""
						def proteins = alias.gene.proteins
						proteinNames = proteins.collect{it.name}
						if(proteinNames){
							proteinNames.each{ pname ->
								queryStringToCall += "$pname OR "
							}
							searchTerm = queryStringToCall.substring(0,queryStringToCall.lastIndexOf('OR'))
							println searchTerm
							
						}
						else{
							println "no proteins found"
						}
					}
					//if weight is specified , add to search
					if(cmd.molWeightLow || cmd.molWeightHigh){
						println "constrain by molecular weight"
						if(!cmd.molWeightLow){
							cmd.molWeightLow = 0.0
						}
						if(!cmd.molWeightHigh){
							cmd.molWeightHigh = 99999.0
						}
						targets = Molecule.search{
							queryString(searchTerm)
							must(between("weight",cmd.molWeightLow.toDouble(),cmd.molWeightHigh.toDouble(),true))
						}
						chain(action:index,model:[ligands:targets],params:[molWeightLow:params.molWeightLow,molWeightHigh:params.molWeightHigh,entityName:params.entity])
						return
					}else{
						targets = Molecule.search{
							queryString(searchTerm)
						}
						chain(action:index,model:[ligands:targets],params:[molWeightLow:params.molWeightLow,molWeightHigh:params.molWeightHigh,entityName:params.entity])
						return
					}
				}catch (SearchEngineQueryParseException ex) { 
					 	println ex
						flash.message = "Error ocurred during search"
						redirect(action:index)
						return 
				}
				
			}else{
				flash.message = "Please select some search parameters"
				redirect(action:index)
				return
			}
		}
	}
	
	def loadSimilar = {
		if(params.moleculeSelector){
			redirect(action:show, id:params.moleculeSelector)
			return
		}
	}
	
	def relevantTerms = {
		def searchResult = []
		if (!params.q?.trim()) { 
			render ""
		}else{
			try { 
				def terms = []
				terms << searchableService.termFreqs("name")
				terms << searchableService.termFreqs("symbol")
				terms.flatten().each{
					if(it.term.contains(params.q.trim()))
						searchResult << it.term
				}
				render searchResult as JSON
			 } catch (SearchEngineQueryParseException ex) { 
				println ex
				return []
			 }
		}
	}
	
	def show = {
			println params
			def moleculeTarget
			def similarTargets = []
			def molTarId 
			if(params.id){
				moleculeTarget = MoleculeTarget.get(params.id)
				molTarId = moleculeTarget.id
			}
			if(moleculeTarget){
				similarTargets = MoleculeTarget.findAllByProtein(moleculeTarget.protein)
				println similarTargets
				def desiredTarget = similarTargets.find{
					it.id == molTarId
				}
				println desiredTarget
				[moleculeTarget:desiredTarget,similarTargets:similarTargets]
			}	
		}
		
	def display = {
		try{
			def file = new File(grailsApplication.config.structuresPath + params.inputFile)
			byte[] fileBytes = file.readBytes()
				response.outputStream << fileBytes
		}catch(java.io.FileNotFoundException fnf){
			println fnf.toString()
			render "File ($params.inputFile) was not found...is the file name correct?"
		}
	}
}