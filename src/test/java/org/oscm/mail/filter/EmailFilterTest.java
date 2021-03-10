package org.oscm.mail.filter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.oscm.mail.json.Email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EmailFilterTest {

  @ParameterizedTest
  @MethodSource("expectedEmails")
  public void testFilterEmails(int expectedEmails, List<String> addresses) {

    // given
    List<Email> emails = new ArrayList<>();
    emails.add(givenEmail("admin@oscm.org", "test@oscm.org"));
    emails.add(givenEmail("admin@oscm.org", "customer@oscm.org"));
    emails.add(givenEmail("support@oscm.org", "admin@oscm.org"));
    emails.add(givenEmail("support@oscm.org", "supplier@oscm.org"));
    emails.add(givenEmail("supplier@oscm.org", "admin@oscm.org"));

    // when
    EmailFilter emailFilter = new EmailFilter();
    List<Email> filteredEmails = emailFilter.filterEmails(emails, addresses);

    // then
    assertThat(filteredEmails).hasSize(expectedEmails);
  }

  private static Stream<Arguments> expectedEmails() {

    return Stream.of(
        arguments(4, Arrays.asList("admin@oscm.org")),
        arguments(2, Arrays.asList("support@oscm.org")),
        arguments(2, Arrays.asList("supplier@oscm.org")),
        arguments(1, Arrays.asList("test@oscm.org")),
        arguments(5, Arrays.asList("admin@oscm.org", "supplier@oscm.org")),
        arguments(3, Arrays.asList("customer@oscm.org", "supplier@oscm.org")));
  }

  private static Email givenEmail(String from, String to) {

    Email email = new Email();
    HashMap<String, String> headers = new HashMap<>();
    headers.put("from", from);
    headers.put("to", to);
    email.setHeaders(headers);
    return email;
  }
}
