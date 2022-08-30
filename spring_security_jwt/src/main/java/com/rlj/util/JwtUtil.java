package com.rlj.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil
{

	// treat the secret key similar to Base64 encoded key if you're using
	// deprecated overload of setSigningKey()
	private final String SECRET_KEY = "mypersonalkeywhichshouldbereallyreallyreallylong";

	public String extractUsername(String token)
	{
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token)
	{
		// setSigningKey(String) is deprecated
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails)
	{
		Map<String, Object> claims = new HashMap<>();  // fill details (claims) here
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject)
	{
		return Jwts.builder().setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60 * 10))  // ~10 hours
			// this overload of signWith() here is deprecated
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails)
	{
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
