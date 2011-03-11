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
	
	def sketchSearch = {
		log.debug params
		def ligands
		def count = new Long(0)
		def search = false
		if(chainModel){
			search = true;
			ligands = chainModel["ligands"]
			count = chainModel["count"]
			ligands.each{
				it.refresh()
			}
			log.debug "results for sketch found, results are $ligands.size with a total count of $count"
		}
		[ligands:ligands,count:count,search:search,params:[params.page,params.offset,params.smiles]]
	}
	
	def searchLigandsFromSketch = {
		session.molCommand = null
		session.smiles = params.smiles
		def offset = 0
		//grab my studies to filter
		def userMemberships = session.myCollaborationGroups
		def smiles
		if(params.smiles){
			smiles = params.smiles
			log.debug "search for smiles for $smiles in groups, $userMemberships"
		}
		if(params.offset){
			offset = params.offset.toLong()
		}
		def targets = []
		def count = []
		def groups = []
		String groupHQL = "SELECT distinct collaborationGroup FROM CollaborationGroup collaborationGroup " + 
		"WHERE collaborationGroup.name in (:groupNames) "
		groups = CollaborationGroup.executeQuery(groupHQL,[groupNames:userMemberships])
		if(groups){
			String listHQL = "SELECT distinct molecule FROM Molecule molecule " + 
			"WHERE molecule.smiles like '%" + smiles + "%' " + 
			"AND molecule.protectionGroup in (:groups) " + 
			"ORDER BY molecule.weight desc"
			targets = Molecule.executeQuery(listHQL,[groups:groups, max:10, offset:offset])
			String listHQL2 = "SELECT count(distinct molecule.id) FROM Molecule molecule " + 
			"WHERE molecule.smiles like '%" + smiles + "%' " +
			"AND molecule.protectionGroup in (:groups) "
			count = Molecule.executeQuery(listHQL2,[groups:groups])
		}
		
		/**targets = Molecule.search(params,{
			must(queryString(smiles), 'escape:true')
			must(queryString(orString))
		})**/
		log.debug targets 
		log.debug "count is $count"
		if(!params.offset){
			chain(action:sketchSearch,model:[ligands:targets,count:count[0]],params:[page:'sketch',smiles:params.smiles,offset:0])
			return
		}
		else{
			chain(action:sketchSearch,model:[ligands:targets,count:count[0]],params:[page:'sketch',smiles:params.smiles,offset:params.offset])
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
					flash.message = "Invalid character, $cmd.entity found during search"
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
			//def user = GDOCUser.findByUsername(session.userId)
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
			if(params.id && params.id != "JmolApplet"){
				def id = new Long(params.id)
				moleculeTarget = MoleculeTarget.get(id)
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