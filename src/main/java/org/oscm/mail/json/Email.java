package org.oscm.mail.json;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Email {

  private String id;
  private String text;
  private String subject;
  private LocalDateTime time;
  private Map<String, String> headers;
}
