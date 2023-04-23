package com.chibuisi.springsecapp.config;

import com.chibuisi.springsecapp.exception.AuthEntryPointJwt;
import com.chibuisi.springsecapp.filter.JwtRequestFilter;
import com.chibuisi.springsecapp.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable CSRF (cross site request forgery)
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .antMatchers("/authenticate", "/", "/users/signup", "/index", "/home").permitAll()
                .antMatchers("/admin", "/topic", "/template", "/payment", "/mail").hasRole("ADMIN")
                .antMatchers("/topicitem").hasAnyRole("ADMIN", "COACH", "DEVELOPER")
                .antMatchers("/user", "/subscription", "/schedule").hasAnyRole("USER", "ADMIN", "COACH", "DEVELOPER")
                .antMatchers("/manager").hasAnyRole("MANAGER", "ADMIN")
                .anyRequest().authenticated()
//				.and().oauth2Login()
                .and().httpBasic();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //the jwt filter will apply before username/password filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //handle unauthorized exceptions
        http.exceptionHandling().authenticationEntryPoint(authEntryPointJwt);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public/**")
                .antMatchers("/static/**");
    }

    //this bean is important for the authentication manager we autowire in service
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
