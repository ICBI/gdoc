eventConfigureTomcat = { tomcat -> 
        def ctx=tomcat.host.findChild(serverContextPath) 
        ctx.useHttpOnly = false 
}