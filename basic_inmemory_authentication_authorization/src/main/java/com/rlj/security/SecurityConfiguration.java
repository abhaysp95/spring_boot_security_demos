package com.rlj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
	@Bean
	// public InMemoryUserDetailsManager userDetailsService()
	public UserDetailsService userDetailsService()  // UserDetailsManager is extension of UserDetailsService
	{
		/* UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("foo")
			.password("bar")
			.roles("USER")
			.build(); */

		UserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("foo")
				.password(encoder().encode("bar"))
				.roles("USER")
				.build());

		manager.createUser(User.withUsername("bar")
				.password(encoder().encode("foo"))
				.roles("ADMIN")
				.build());

		return manager;
	}

	@Bean
	public static PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
			.antMatchers("/**").permitAll()
			.and().formLogin()
			.and().logout().permitAll();

		return http.build();
	}
}
