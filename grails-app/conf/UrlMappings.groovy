class UrlMappings {
    static mappings = {
	  "/genomeBrowser/data/$chromosome/$dataType/$id" {
		controller = "genomeBrowser"
		action = "data"
	  }
	  "/"(controller:'home',action:'index') 
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "500"(view:'/error')
	}
}
