package com.rlj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rlj.service.AppUserDetailsService;

@Service
public class HomeAuthenticationProvider implements AuthenticationProvider
{

	@Autowired
	private AppUserDetailsService appUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO Auto-generated method stub
		String username = (null == authentication.getPrincipal() ? "NONE PROVIDED" : authentication.getName());
		String password = (null == authentication.getPrincipal() ? "NONE PROVIDED" : authentication.getCredentials().toString());
		if (username.isEmpty() || password.isEmpty()) {
			throw new BadCredentialsException("invalid login details");
		}
		UserDetails user = null;
		try {
			user = this.appUserDetailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException ex) {
			throw new BadCredentialsException("invalid login details");
		}

        return createSuccessfulAuthentication(authentication, user, password);
    }

	private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user, final String rawPassword)
	{
		if (passwordEncoder.matches(rawPassword, user.getPassword())) {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					user.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
			return token;
		}
		throw new BadCredentialsException("Bad credentials");
	}

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
