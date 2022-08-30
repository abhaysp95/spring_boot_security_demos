package com.rlj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rlj.service.MyUserDetailsService;

@EnableWebSecurity
public class DeprecatedSecurityConfiguration extends WebSecurityConfigurerAdapter
{

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(myUserDetailsService);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception
	{
		return super.authenticationManager();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		// authentication mandatory for all endpoints except "/authenticate"
		http.csrf().disable()
			.authorizeRequests().antMatchers("/authenticate").permitAll()
			.anyRequest().authenticated();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}
}
