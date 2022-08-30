package com.rlj.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rlj.model.AuthenticationRequest;
import com.rlj.model.AuthenticationResponse;
import com.rlj.util.JwtUtil;

@RestController
public class HomeController
{
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/home")
	public String home()
	{
		return "<h1>Welcome Home</h1>";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {

		Logger.getGlobal().info(request.toString());

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			Logger.getGlobal().info("here");
		} catch (BadCredentialsException ex) {
			Logger.getGlobal().info("there");
			throw new Exception("Incorrect username or password", ex);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		Logger.getGlobal().info(String.format("%s, %s", userDetails.getUsername(), userDetails.getPassword()));

		final String jwt = jwtUtil.generateToken(userDetails);

		Logger.getGlobal().info(jwt);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}

