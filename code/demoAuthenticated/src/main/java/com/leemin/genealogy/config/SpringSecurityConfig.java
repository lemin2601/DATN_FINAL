package com.leemin.genealogy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/home", "/about").permitAll()
            .antMatchers("/admin/**").hasAnyRole("ADMIN")
            .antMatchers("/user/**").hasAnyRole("USER")
            .anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
//            .and().logout().permitAll()
            .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
            .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    // create two users, admin and user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("{noop}pwd").roles("USER")
                .and()
                .withUser("admin").password("{noop}pwd").roles("ADMIN");
//        auth.inMemoryAuthentication()
//            .withUser("user")
//            .password("password")
//            .roles("USER")
//            .and()
//            .withUser("admin")
//            .password("password")
//            .roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers("/webjars/**");
        web.ignoring()
           .antMatchers("/css/**", "/fonts/**", "/libs/**");
    }
}