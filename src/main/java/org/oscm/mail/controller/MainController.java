package org.oscm.mail.controller;

import org.oscm.mail.auth.AuthenticationContext;
import org.oscm.mail.json.Email;
import org.oscm.mail.provider.EmailProvider;
import org.oscm.mail.provider.MaildevEmailProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.List;

@Controller
public class MainController {

  @Autowired private AuthenticationContext authContext;

  @Value("${hostname}")
  private String hostname;

  @RequestMapping("/")
  public String index(HttpServletRequest req) throws UnknownHostException {
    return "redirect:https://" + this.hostname + "/mail";
  }

  @RequestMapping("/email")
  public String email(Model model) {

    EmailProvider provider = new MaildevEmailProvider(new RestTemplate());
    List<Email> emails = provider.getAllEmails();

    model.addAttribute("emails", emails);
    return "emails";
  }
}
