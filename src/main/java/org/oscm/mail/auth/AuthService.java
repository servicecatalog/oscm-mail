/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2021
 *******************************************************************************/
package org.oscm.mail.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class AuthService {

  private static final String ANONYMOUS_USER = "anonymousUser";

  boolean isUserAlreadyAuthenticated() {
    Authentication authentication = getAuthentication();
    return !ANONYMOUS_USER.equals(authentication.getPrincipal());
  }

  public Set<String> getUserList() {
    Authentication authentication = getAuthentication();

    if (authentication.getPrincipal() instanceof Set) {
      return (Set<String>) authentication.getPrincipal();
    }
    return Collections.emptySet();
  }

  Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
