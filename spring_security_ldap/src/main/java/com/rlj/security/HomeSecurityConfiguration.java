package com.rlj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.config.ldap.LdapPasswordComparisonAuthenticationManagerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class HomeSecurityConfiguration
{

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.anyRequest().fullyAuthenticated().
			and().formLogin();

		return http.build();
	}

	@Bean
	public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean()
	{
		EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
			EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();

		contextSourceFactoryBean.setPort(8389);
		return contextSourceFactoryBean;

		// NOTE: you don't need to specify ldif. Default is from classpath*:*.ldif
	}

	@Bean
	public AuthenticationManager ldapAuthenticationManager(BaseLdapPathContextSource contextSource)
	{
		// neither this, nor LdapBindAuthenticationManagerFactory are working
		LdapPasswordComparisonAuthenticationManagerFactory factory =
			new LdapPasswordComparisonAuthenticationManagerFactory(contextSource, new BCryptPasswordEncoder(11));

		factory.setUserDnPatterns("uid={0},ou=people");
		return factory.createAuthenticationManager();
	}
}
