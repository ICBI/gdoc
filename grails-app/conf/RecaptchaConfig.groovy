recaptcha {
    // These keys are generated by the ReCaptcha service
	publicKey = "6LfnJ74SAAAAAKHvX-zAx5YKhWjaqOgfWMP1RByk"
	privateKey = "6LfnJ74SAAAAAAvt4qrAPL4etOrRXvlnIWLsklPP"

	// Include the noscript tags in the generated captcha
	includeNoScript = true
}

mailhide {
    // Generated by the Mailhide service
    publicKey = ""
    privateKey = ""
}

environments {
  development {
    recaptcha {
      // Set to false to disable the display of captcha
      enabled = true

      // Communicate using HTTPS
      useSecureAPI = false
    }
  }
  devserver {
    recaptcha {
      // Set to false to disable the display of captcha
      enabled = true

      // Communicate using HTTPS
      useSecureAPI = false
    }
  }
  demo {
    recaptcha {
      // Set to false to disable the display of captcha
      enabled = true

      // Communicate using HTTPS
      useSecureAPI = true
    }
  }
  stage {
    recaptcha {
      // Set to false to disable the display of captcha
      enabled = true

      // Communicate using HTTPS
      useSecureAPI = true
    }
  }
  production {
    recaptcha {
      // Set to false to disable the display of captcha
      enabled = true

      // Communicate using HTTPS
      useSecureAPI = true
    }
  }
}