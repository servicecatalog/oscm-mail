package org.oscm.mail.controller;

import org.oscm.mail.auth.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private static final String ANONYMOUS_USER = "anonymousUser";

  @Autowired private AuthenticationContext authContext;

  @GetMapping("/auth")
  public ResponseEntity isAuthenticated() {

    Authentication authentication = authContext.getAuthentication();

    if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    } else {
      return ResponseEntity.ok().build();
    }
  }
}
