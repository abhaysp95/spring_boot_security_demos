// deprecated

/* package com.rlj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class DeprecatedSecurityConfiguration extends WebSecurityConfigurerAdapter
{

	@Override
	protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception
	{
		authBuilder.inMemoryAuthentication()
			.withUser("foo")
			.password("foop")
			.roles("USER");
	}

	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{

		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN", "USER")
			.antMatchers("/**").permitAll()
			.and().formLogin();
	}
} */


		/* http.authorizeRequests()
			// .antMatchers("/", "static/css", "static/js").permitAll()
			.antMatchers("/").permitAll()
			.antMatchers("/**").hasRole("ADMIN")
			.and().formLogin(); */
