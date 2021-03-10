package org.oscm.mail.provider;

import org.oscm.mail.json.Email;

import java.util.List;

public interface EmailProvider {

  String URL_ALL_EMAILS = "http://localhost:8080/data/emails";

  List<Email> getAllEmails();
}
