package com.rlj.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rlj.service.MyUserDetailsService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException
	{
        // TODO Auto-generated method stub
		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		if ((null != authorizationHeader && !authorizationHeader.isEmpty())
				&& authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwt);
		}

		if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
    }



}
