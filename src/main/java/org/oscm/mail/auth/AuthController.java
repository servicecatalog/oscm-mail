/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2021
 *
 * <p>**************************************************************************
 */
package org.oscm.mail.auth;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  @Autowired private AuthService authContext;

  @Autowired private HttpSession httpSession;

  @GetMapping("/auth")
  public ResponseEntity isAuthenticated() {
    SecurityContext ctx =
        (SecurityContext)
            httpSession.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

    LOGGER.info(String.format("-------------------------------------------------"));
    LOGGER.info(String.format("/auth - got session %s ", httpSession.getId()));

    if (ctx != null) {
      LOGGER.info(
          String.format(
              "/auth - found session %s for: %s",
              httpSession.getId(), ctx.getAuthentication().getPrincipal()));
      SecurityContextHolder.getContext().setAuthentication(ctx.getAuthentication());

      Authentication a = ctx.getAuthentication();
      boolean isAnonymous = (a instanceof AnonymousAuthenticationToken);
      if (!isAnonymous) {
        if (a.isAuthenticated()) {
          return ResponseEntity.ok().build();
        } else if (LOGGER.isDebugEnabled()) {
          LOGGER.info(String.format("Not authenticated: %s", a.getPrincipal()));
        }
      }
    }
    LOGGER.info("/auth returned HTTP 401");
    LOGGER.info(String.format("-------------------------------------------------"));
    return ResponseEntity.status(401).build();
  }
}
