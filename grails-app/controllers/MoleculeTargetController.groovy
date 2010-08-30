import org.compass.core.engine.SearchEngineQueryParseException
import grails.converters.*

class MoleculeTargetController {
	def drugDiscoveryService
	def annotationService
	def searchableService
	def collaborationGroupService
	
	def index = {
		log.debug params
		def ligands
		def search = false
		if(chainModel){
			search = true;
			ligands = chainModel["ligands"]
			if(ligands.results){
				def molIds = ligands.results.collect{it.id}
				log.debug "results found, results are $ligands.results.size()"
				ligands.results = Molecule.getAll(ligands.results.collect{it.id})
			}
		}
		[ligands:ligands,search:search,params:[params.page,params.offset,params.entityName]]
	
	}
	
	
	def page = {
		def targets = []
		if(session.molCommand && params.offset){
			handleMoleculeSearch(session.molCommand,params)
		}else{
			handleMoleculeSearch(null,params.offset)
		}
		//chain(action:index,model:[ligands:targets])
	}
	
	def searchLigandsFromSketch = {
		session.molCommand = null
		session.smiles = params.smiles
		def targets = Molecule.search(params.smiles,params)
		log.debug targets
		if(!params.offset){
			chain(action:index,model:[ligands:targets],params:[page:'sketch',smiles:params.smiles])
			return
		}
		else{
			chain(action:index,model:[ligands:targets],params:[page:'sketch',smiles:params.smiles,offset:params.offset])
			return
		}
	}
	
	def searchLigands = { MoleculeTargetCommand cmd ->
		log.debug "Entity: " + cmd.entity
		log.debug cmd.errors
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index',params:params)
			return
		}else{
			session.molCommand = cmd
			handleMoleculeSearch(cmd,[offset:0,max:10])
		}
	}
	
	def handleMoleculeSearch(cmd, params){
		if(!session.molCommand && session.smiles){
			log.debug "this needs to be redirected to sketch, offset $params"
			chain(action:searchLigandsFromSketch,params:[page:'sketch',smiles:session.smiles,offset:params])
			return
		}
		//grab my studies to filter
		def userMemberships = session.myCollaborationGroups
		def orString = userMemberships.join(" OR ")
		
		def targets = []
		if(cmd.entity || cmd.molWeightLow || cmd.molWeightHigh || cmd.affinity){
			
			if(!cmd.entity && (cmd.molWeightLow || cmd.molWeightHigh)){
				if(!cmd.molWeightLow){
					cmd.molWeightLow = new Float(0.0)
				}
				if(!cmd.molWeightHigh){
					cmd.molWeightHigh = new Float(99999.0)
				}
				targets = Molecule.search(params,{
					must(ge("weight",cmd.molWeightLow.toFloat()))
					must(le("weight",cmd.molWeightHigh.toFloat()))
					must(queryString(orString))
				})
				chain(action:index,model:[ligands:targets],params:[molWeightLow:params.molWeightLow,molWeightHigh:params.molWeightHigh,entityName:cmd.entity,offset:params.offset])
				return
			}
			try { 
			def searchTerm = cmd.entity
				//if weight is specified , add to search
				if(cmd.molWeightLow || cmd.molWeightHigh){
					log.debug "constrain by molecular weight"
					if(!cmd.molWeightLow){
						cmd.molWeightLow = 0.0
					}
					if(!cmd.molWeightHigh){
						cmd.molWeightHigh = 99999.0
					}
					targets = Molecule.search(params,{
						must(queryString(searchTerm))
						must(between("weight",cmd.molWeightLow.toDouble(),cmd.molWeightHigh.toDouble(),true))
						must(queryString(orString))
					})
					chain(action:index,model:[ligands:targets],params:[molWeightLow:params.molWeightLow,molWeightHigh:params.molWeightHigh,entityName:cmd.entity,offset:params.offset])
					return
				}else{
					targets = Molecule.search(params,{
						must(queryString(searchTerm))
						must(queryString(orString))
					})
					log.debug targets
					chain(action:index,model:[ligands:targets],params:[molWeightLow:params.molWeightLow,molWeightHigh:params.molWeightHigh,entityName:cmd.entity,offset:params.offset])
					return
				}
			}catch (SearchEngineQueryParseException ex) { 
				 	log.debug ex
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
				terms << Protein.termFreqs("accession")
				terms << GeneAlias.termFreqs("symbol",size:30000)
				terms.flatten().each{
					if(it.term.contains(params.q.trim()))
						searchResult << it.term
				}
				render searchResult as JSON
			 } catch (SearchEngineQueryParseException ex) { 
				log.debug ex
				return []
			 }
		}
	}
	
	def filterLigands(ligandResults){
			//log.debug "unfiltered: $ligandResults"
			//def user = GDOCUser.findByLoginName(session.userId)
			def userMemberships = session.myCollaborationGroups
			
			def results = []
			  ligandResults.each{
					if(it.protectionGroup)
					if(it.protectionGroup.name && userMemberships.contains(it.protectionGroup.name)){
						results << it
					}
			  }
			//log.debug "filtered: $results"
			return results
	}
	
	def show = {
			log.debug params
			def moleculeTarget
			def similarTargets = []
			def molTarId 
			if(params.id){
				moleculeTarget = MoleculeTarget.get(params.id)
				molTarId = moleculeTarget.id
			}
			if(moleculeTarget){
				similarTargets = MoleculeTarget.findAllByProtein(moleculeTarget.protein)
				log.debug similarTargets
				def desiredTarget = similarTargets.find{
					it.id == molTarId
				}
				log.debug "find if molecule of target is permitted by user"
				if(desiredTarget){
					def result = filterLigands(desiredTarget.molecule)
					log.debug "$result was found"
					if(!result){
						log.debug "molecule of target is NOT permitted by user"
						desiredTarget = null
					}
				}
				log.debug desiredTarget
				[moleculeTarget:desiredTarget,similarTargets:similarTargets]
			}	
		}
		
	def display = {
		try{
			if(params.dimension){
				def file
				if(params.dimension=='3D'){
					file = new File(grailsApplication.config.molecule3DstructuresPath + params.inputFile)
				}else{
					file = new File(grailsApplication.config.molecule2DstructuresPath + params.inputFile)
				}
				byte[] fileBytes = file.readBytes()
				response.outputStream << fileBytes
			}
		}catch(java.io.FileNotFoundException fnf){
			log.debug fnf.toString()
			render "File ($params.inputFile) was not found...is the file name correct?"
		}
	}
}