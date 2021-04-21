/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2021
 *******************************************************************************/
package org.oscm.mail.auth;

import org.oscm.intf.IdentityService;
import org.oscm.mail.client.WSClient;
import org.oscm.types.enumtypes.UserRoleType;
import org.oscm.vo.VOUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OSCMAuthenticationProvider implements AuthenticationProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(OSCMAuthenticationProvider.class);
  private WSClient wsClient;

  public OSCMAuthenticationProvider(WSClient wsClient) {
    this.wsClient = wsClient;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String userKey = authentication.getName();
    String userPwd = authentication.getCredentials().toString();
    String authMode = "INTERNAL";

    try {
      IdentityService identityService = wsClient.getIdentityService(authMode, userKey, userPwd);
      VOUserDetails user = identityService.getCurrentUserDetails();

      if (!userKey.equals(String.valueOf(user.getKey()))
          || user.getEMail() == null
          || user.getEMail().isEmpty()) {
        LOGGER.error(
            "Invalid credentials - provided key: "
                + user.getKey()
                + " instead of: "
                + userKey
                + ", email:"
                + user.getEMail());
        throw new BadCredentialsException("Invalid credentials");
      }
      Set<String> emails = new HashSet<>();
      emails.add(user.getEMail());

      if (user.getUserRoles().contains(UserRoleType.ORGANIZATION_ADMIN)) {
        emails.addAll(
            identityService.getUsersForOrganization().stream()
                .map(VOUserDetails::getEMail)
                .collect(Collectors.toSet()));
      }

      return new UsernamePasswordAuthenticationToken(emails, userPwd, new ArrayList<>());
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new AuthenticationServiceException(
          "Authentication failed - invalid user key or/and password");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
