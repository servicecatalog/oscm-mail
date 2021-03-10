package org.oscm.mail.filter;

import org.oscm.mail.json.Email;

import java.util.List;
import java.util.stream.Collectors;

public class EmailFilter {

  public List<Email> filterEmails(List<Email> emailsToFilter, List<String> addressees) {

    List<Email> filteredEmails =
        emailsToFilter.stream()
            .filter(
                email ->
                    addressees.contains(email.getHeaders().get("from"))
                        || addressees.contains(email.getHeaders().get("to")))
            .collect(Collectors.toList());
    return filteredEmails;
  }
}
