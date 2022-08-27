package com.rlj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.rlj.passutil.Argon2PassEncoder;

@Configuration
@EnableWebSecurity
public class HomeSecurityConfiguration
{

	@Bean
	public UserDetailsService userDetailsService()
	{
		UserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("foo")
				.password(passwordEncoder().encode("bar"))
				.roles("USER").
				build());

		manager.createUser(User.withUsername("bar")
				.password(passwordEncoder().encode("foo"))
				.roles("ADMIN")
				.build());

		return manager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN", "USER")
			.antMatchers("/**").permitAll()
			.and().formLogin()
			.and().logout().permitAll();

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		int saltLen = 32;
		int hashLen = 64;
		int iterations = 10;
		int memory = 1 << 15; // ~32mb
		int parallelism = Runtime.getRuntime().availableProcessors();


		return new Argon2PassEncoder(saltLen, hashLen, parallelism, memory, iterations);  // custom password encoder
	}
}
