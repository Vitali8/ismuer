package com.upfordown.ismuer.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The type Security config.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().disable()
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .authorizeRequests().anyRequest().permitAll()
        ;
    }
}
