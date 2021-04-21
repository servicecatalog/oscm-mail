package org.oscm.mail.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Spy private AuthService authService = new AuthService();

  @Test
  void testIsUserAlreadyAuthenticated_whenAnonymousUser() {
    // given
    Authentication authentication = mock(Authentication.class);
    when(authService.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn("anonymousUser");

    // when
    boolean authenticated = authService.isUserAlreadyAuthenticated();

    // then
    assertFalse(authenticated);
  }

  @Test
  void testIsUserAlreadyAuthenticated_whenUsersExist() {
    // given
    Authentication authentication = mock(Authentication.class);
    when(authService.getAuthentication()).thenReturn(authentication);
    HashSet<String> users = new HashSet<>();
    users.add("test1@oscm.org");
    users.add("test2@oscm.org");
    when(authentication.getPrincipal()).thenReturn(users);

    // when
    boolean authenticated = authService.isUserAlreadyAuthenticated();

    // then
    assertTrue(authenticated);
  }

  @Test
  void testGetUserList_whenAnonymousUser() {
    // given
    Authentication authentication = mock(Authentication.class);
    when(authService.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn("anonymousUser");

    // when
    Set<String> userList = authService.getUserList();

    // then
    assertThat(userList).isEmpty();
  }

  @Test
  void testGetUserList_whenUsersExist() {
    // given
    Authentication authentication = mock(Authentication.class);
    when(authService.getAuthentication()).thenReturn(authentication);
    HashSet<String> users = new HashSet<>();
    users.add("test1@oscm.org");
    users.add("test2@oscm.org");
    when(authentication.getPrincipal()).thenReturn(users);

    // when
    Set<String> userList = authService.getUserList();

    // then
    assertThat(userList).containsExactly("test1@oscm.org", "test2@oscm.org");
  }
}
