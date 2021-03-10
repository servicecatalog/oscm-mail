package org.oscm.mail.provider;

import org.oscm.mail.json.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MaildevEmailProvider implements EmailProvider{

  private RestTemplate restTemplate;

  public MaildevEmailProvider(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<Email> getAllEmails() {

    ResponseEntity<Email[]> response = restTemplate.getForEntity(URL_ALL_EMAILS, Email[].class);
    return Arrays.asList(response.getBody());
  }
}
