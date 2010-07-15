import grails.converters.*

class TagService{
	
	def addTag(type,id,tag){
		def klazz = Thread.currentThread().contextClassLoader.loadClass(type)
		//log.debug "LOOKING UP $type : $id to add tag: $tag"
		def taggableThing = klazz.get(id)
		if(taggableThing){
			taggableThing.addTag(tag)
			return taggableThing;
		}else{
			return null;
		}
		
	}
	
	def removeTag(type,id,tag){
		def klazz = Thread.currentThread().contextClassLoader.loadClass(type)
		log.debug "LOOKING UP $type : $id to remove tag: $tag"
		def taggableThing = klazz.get(id)
		if(taggableThing){
			taggableThing.removeTag(tag)
			taggableThing.refresh()
			log.debug "removed tag: $tag from $type : $id"
			return taggableThing;
		}else{
			return null;
		}
		
	}
	
	
}