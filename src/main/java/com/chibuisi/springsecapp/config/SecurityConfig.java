package com.chibuisi.springsecapp.config;

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
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chibuisi.springsecapp.filter.JwtRequestFilter;
import com.chibuisi.springsecapp.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//		.withUser("kelvin").password("server").roles("ADMIN")
//		.and().withUser("chibuisi").password("server").roles("USER")
//		.and().withUser("chris").password("2020").roles("MANAGER");
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

	    // Disable CSRF (cross site request forgery)
	    http.csrf().disable();

		http.authorizeRequests()
		.antMatchers("/admin").hasRole("ADMIN")
		.antMatchers("/user").hasRole("USER")
		.antMatchers("/manager").hasRole("MANAGER")
		.antMatchers("/authenticate","/","/users/signup", "/index", "/home").permitAll()
		.antMatchers("/oauth2/**").permitAll()
				.anyRequest().authenticated()
				.and().oauth2Login()
		.and().formLogin();
		
		// No session will be created or used by spring security
	    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	  
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
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
				.antMatchers("/static/**")

	        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
	        .and()
	        .ignoring()
	        .antMatchers("/h2-console/**/**");;
	  }

//	  @Bean
//	  public PasswordEncoder passwordEncoder() {
//	    return new BCryptPasswordEncoder(12);
//	  }
	  
	  //this bean is important for the authentication manager we autowire in controller
	  @Override
	  @Bean
	  public AuthenticationManager authenticationManagerBean() throws Exception{
		  return super.authenticationManagerBean();
	  }

	
}
