class UrlMappings {
    static mappings = {
	 "/genomeBrowser/$action?/$chromosome?/$dataType?/$id?**" {
		controller = "genomeBrowser"
	  }
	 /* "/"(controller:'home',action:'index') 
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "500"(view:'/error')*/
	}
}
