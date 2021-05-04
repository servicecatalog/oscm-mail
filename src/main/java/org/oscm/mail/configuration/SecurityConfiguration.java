/**
 * ****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2021
 *
 * <p>*************************************************************************
 */
package org.oscm.mail.configuration;

import org.oscm.mail.auth.OSCMAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired private OSCMAuthenticationProvider authProvider;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider);
  }

  @Autowired SecurityHandler successHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests()
        .antMatchers("/auth")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .and()
        .logout()
        .permitAll()
        .and()
        .sessionManagement()
        .sessionFixation()
        .migrateSession()
        .and()
        .csrf()
        .disable();
  }
}
