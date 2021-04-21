/*******************************************************************************
 *  Copyright FUJITSU LIMITED 2021
 *******************************************************************************/
package org.oscm.mail;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(OscmMailApplication.class);
  }
}
