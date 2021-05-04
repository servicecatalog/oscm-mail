/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2021
 *
 * <p>**************************************************************************
 */
package org.oscm.mail.controller;

import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.oscm.mail.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @Autowired private AuthService authService;

  @Value("${OSCM_MAIL_URL}")
  private String mailUrl;

  @Autowired private HttpSession httpSession;

  @GetMapping("/")
  public RedirectView index(RedirectAttributes attributes, HttpServletResponse res) {

    Set<String> userList = authService.getUserList();
    SecurityContext ctx = SecurityContextHolder.getContext();

    if (ctx != null) {
      logUserAuthenticated(ctx);
      httpSession.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ctx);
      httpSession.setMaxInactiveInterval(15 * 60);
      attributes.addFlashAttribute("token", ctx.getAuthentication());
      addSessionCookie(res);
    }

    LOGGER.info(String.format("Redirecting to %s with following users %s", this.mailUrl, userList));

    if (userList.isEmpty()) {
      return new RedirectView(this.mailUrl);
    }
    attributes.addAttribute("filter", String.join(",", userList));
    return new RedirectView(this.mailUrl + "/user");
  }

  private void logUserAuthenticated(SecurityContext ctx) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info(String.format("-------------------------------------------------"));
      LOGGER.info(
          String.format(
              "/ - found session %s for: %s ",
              httpSession.getId(), ctx.getAuthentication().getPrincipal()));

      LOGGER.info(String.format("-------------------------------------------------"));
    }
  }

  private void addSessionCookie(HttpServletResponse res) {
    Cookie cookie = new Cookie("JSESSIONID", httpSession.getId());
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    res.addCookie(cookie);
  }
}
