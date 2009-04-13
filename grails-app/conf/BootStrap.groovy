class BootStrap {

     def init = { servletContext ->
		new GDOCUser(username:'gdocUser').save();
     }
     def destroy = {
     }
} 