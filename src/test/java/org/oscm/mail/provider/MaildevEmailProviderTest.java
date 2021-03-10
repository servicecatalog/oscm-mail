package org.oscm.mail.provider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oscm.mail.json.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaildevEmailProviderTest {

  @Mock private RestTemplate restTemplate;
  @InjectMocks private MaildevEmailProvider provider;

  @Test
  public void testGetAllEmails() {

    // given
    Email email = new Email();
    email.setId("id1");
    email.setSubject("testSubject");
    email.setText("testContent");

    ResponseEntity entity = mock(ResponseEntity.class);
    when(restTemplate.getForEntity(anyString(), any())).thenReturn(entity);
    when(entity.getBody()).thenReturn(new Email[] {email});

    // when
    List<Email> allEmails = provider.getAllEmails();

    // then
    assertThat(allEmails).contains(email);
  }
}
