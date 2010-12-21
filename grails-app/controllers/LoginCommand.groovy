class LoginCommand {
   String login_name
   String password
   String desiredPage
   static constraints = {
           login_name(blank:false, minSize:3)
           password(blank:false, minSize:3)
   }
}