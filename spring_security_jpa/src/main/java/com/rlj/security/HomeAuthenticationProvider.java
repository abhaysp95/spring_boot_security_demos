package com.rlj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rlj.service.AppUserDetailsService;

public class HomeAuthenticationProvider implements AuthenticationProvider
{

	@Autowired
	AppUserDetailsService appUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO Auto-generated method stub
		String username = (null == authentication.getPrincipal() ? "NONE PROVIDED" : authentication.getName());
		if (username.isEmpty()) {
			throw new BadCredentialsException("invalid login details");
		}
		UserDetails user = null;
		try {
			user = this.appUserDetailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException ex) {
			throw new BadCredentialsException("invalid login details");
		}

        return createSuccessfulAuthentication(authentication, user);
    }

	private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user)
	{
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				user.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
		return token;
	}

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return true;
    }

}
