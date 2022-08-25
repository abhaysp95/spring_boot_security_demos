package com.rlj.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class HomeSecurityConfiguration
{

	@Bean
	public DataSource dataSource()
	{
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			// .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)  // database building by retrieving
																	  // users details
			.build();
	}

	@Bean
	public UserDetailsService users(DataSource dataSource)
	{
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

		/* manager.createUser(User.withUsername("foo")
				.password(passwordEncoder().encode("bar"))
				.roles("USER")
				.build());
		manager.createUser(User.withUsername("bar")
				.password(passwordEncoder().encode("foo"))
				.roles("ADMIN")
				.build()); */

		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
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
}
