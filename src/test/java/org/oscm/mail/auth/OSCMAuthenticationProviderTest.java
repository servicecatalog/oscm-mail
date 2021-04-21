/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2021
 *******************************************************************************/
package org.oscm.mail.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oscm.intf.IdentityService;
import org.oscm.mail.client.WSClient;
import org.oscm.types.enumtypes.UserRoleType;
import org.oscm.vo.VOUserDetails;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OSCMAuthenticationProviderTest {

  @Mock WSClient wsClient;
  @Mock IdentityService identityService;
  @Mock Authentication authentication;

  @BeforeEach
  void setUp() {}

  @Test
  void testAuthenticate_withValidUser() throws Exception {
    // given
    when(wsClient.getIdentityService(anyString(), anyString(), anyString()))
        .thenReturn(identityService);
    when(identityService.getCurrentUserDetails()).thenReturn(givenUser(1000, "email@test.com"));
    when(authentication.getName()).thenReturn("1000");
    when(authentication.getCredentials()).thenReturn("customPwd");

    OSCMAuthenticationProvider oscmAuthenticationProvider =
        new OSCMAuthenticationProvider(wsClient);

    // when
    Authentication oscmAuthentication = oscmAuthenticationProvider.authenticate(authentication);

    // then
    assertThat((Set) oscmAuthentication.getPrincipal()).containsExactly("email@test.com");
  }

  @Test
  void testAuthenticate_withOtherUsersInOrg() throws Exception {
    // given
    when(wsClient.getIdentityService(anyString(), anyString(), anyString()))
        .thenReturn(identityService);
    when(identityService.getCurrentUserDetails()).thenReturn(givenUser(1000, "email@test.com"));
    when(identityService.getUsersForOrganization())
        .thenReturn(
            Arrays.asList(
                givenUser(10000, "email2@test.com"), givenUser(10001, "email3@test.com")));
    when(authentication.getName()).thenReturn("1000");
    when(authentication.getCredentials()).thenReturn("customPwd");

    OSCMAuthenticationProvider oscmAuthenticationProvider =
        new OSCMAuthenticationProvider(wsClient);

    // when
    Authentication oscmAuthentication = oscmAuthenticationProvider.authenticate(authentication);

    // then
    assertThat((Set) oscmAuthentication.getPrincipal())
        .contains("email@test.com", "email2@test.com", "email3@test.com");
  }

  @Test
  void testAuthenticate_withEmptyEmail() throws Exception {
    // given
    when(wsClient.getIdentityService(anyString(), anyString(), anyString()))
        .thenReturn(identityService);
    VOUserDetails user = new VOUserDetails();
    user.setKey(1000);
    when(identityService.getCurrentUserDetails()).thenReturn(user);

    when(authentication.getName()).thenReturn("1000");
    when(authentication.getCredentials()).thenReturn("customPwd");

    OSCMAuthenticationProvider oscmAuthenticationProvider =
        new OSCMAuthenticationProvider(wsClient);

    // when and then
    assertThatExceptionOfType(AuthenticationServiceException.class)
        .isThrownBy(() -> oscmAuthenticationProvider.authenticate(authentication));
  }

  private VOUserDetails givenUser(int key, String email) {
    VOUserDetails user = new VOUserDetails();
    user.setKey(key);
    user.setEMail(email);
    HashSet<UserRoleType> roles = new HashSet<>();
    roles.add(UserRoleType.ORGANIZATION_ADMIN);
    user.setUserRoles(roles);
    return user;
  }
}
