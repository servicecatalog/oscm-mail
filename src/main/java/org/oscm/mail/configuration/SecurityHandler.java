/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2021
 *
 * <p>Creation Date: 01.05.2021
 *
 * <p>*****************************************************************************
 */
package org.oscm.mail.configuration;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class SecurityHandler implements AuthenticationSuccessHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityHandler.class);

  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    LOGGER.info(String.format("-------------------------------------------------"));
    LOGGER.info(
        String.format(
            "SecurityHandler: onAuthenticationSuccess - %s", authentication.getPrincipal()));
    LOGGER.info(String.format("-------------------------------------------------"));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    request
        .getSession(true)
        .setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());
  }
}
