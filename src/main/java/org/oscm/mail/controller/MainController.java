/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2021
 *******************************************************************************/
package org.oscm.mail.controller;

import org.oscm.mail.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @Autowired private AuthService authService;

  @Value("${OSCM_MAIL_URL}")
  private String mailUrl;

  @RequestMapping("/")
  public String index() {
    Set<String> userList = authService.getUserList();
    LOGGER.info(String.format("Redirecting to %s with following users %s", this.mailUrl, userList));

    if (userList.isEmpty()) {
      return "redirect:" + this.mailUrl;
    }
    return "redirect:" + this.mailUrl + "/user?filter=" + String.join(",", userList);
  }
}
