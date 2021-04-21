/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2021
 *******************************************************************************/
package org.oscm.mail.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired private AuthService authContext;

  @GetMapping("/auth")
  public ResponseEntity isAuthenticated() {

    if (authContext.isUserAlreadyAuthenticated()) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(401).build();
    }
  }
}
