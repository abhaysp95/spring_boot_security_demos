package com.rlj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.rlj.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class HomeSecurityConfiguration
{

	@Autowired
	HomeAuthenticationProvider homeAuthenticationProvider;

	@Autowired
	public void bindAuthenticationProvider(AuthenticationManagerBuilder auth)
	{
		auth.authenticationProvider(homeAuthenticationProvider);
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN", "USER")
			.antMatchers("/**").permitAll()
			.and().formLogin()
			.and()
				.logout()
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll();

		return http.build();
	}
}
