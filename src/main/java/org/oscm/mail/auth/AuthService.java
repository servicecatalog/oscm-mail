/**
 * ***************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2021
 *
 * <p>************************************************************************
 */
package org.oscm.mail.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class AuthService {

  private static final String ANONYMOUS_USER = "anonymousUser";
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

  boolean isUserAlreadyAuthenticated() {
    Authentication authentication = getAuthentication();
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info(
          String.format(
              "isUserAlreadyAuthenticated -> got principal %s", authentication.getPrincipal()));
    }
    boolean isAuth = authentication.isAuthenticated();
    boolean isAnonymous = (authentication instanceof AnonymousAuthenticationToken);
    String thId = String.valueOf(Thread.currentThread().getId());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info(String.format("-------------------------------------------------"));
      LOGGER.info(String.format("Thread [%s]: Check authenticated", thId));
      LOGGER.info(String.format("Anonymous: %s", String.valueOf(isAnonymous)));
      LOGGER.info(String.format("Authenticated: %s", String.valueOf(isAuth)));
      LOGGER.info(String.format("-------------------------------------------------"));
    }
    return isAuth && !isAnonymous;
  }

  public Set<String> getUserList() {
    Authentication authentication = getAuthentication();
    if (authentication.getPrincipal() instanceof Set) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String thId = String.valueOf(Thread.currentThread().getId());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.info(String.format("Thread: %s", thId));
      }
      return (Set<String>) authentication.getPrincipal();
    }
    return Collections.emptySet();
  }

  Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
