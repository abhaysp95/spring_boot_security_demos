package com.rlj.security;


/** This works but is Deprecated, see another configuration file */

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DeprecatedHomeSecurityConfiguration extends WebSecurityConfigurerAdapter
{

	/* @Autowired
	DataSource dataSource; */

	@Bean
	public DataSource dataSource()
	{
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.build();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.jdbcAuthentication()
			.withDefaultSchema()
			.dataSource(dataSource())
			.withUser(User.withUsername("foo")
					.password(passwordEncoder().encode("bar"))
					.roles("USER"))
			.withUser(User.withUsername("bar")
					.password(passwordEncoder().encode("foo"))
					.roles("ADMIN"));
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN", "USER")
			.antMatchers("/").permitAll()
			.and().formLogin()
			.and().logout().permitAll();
	}
}
