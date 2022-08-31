package com.rlj.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

// go with https://github.com/jwtk/jjwt/issues/617 for any confusion regarding
// signing of secret key

@Service
public class JwtUtil
{

	// treat the secret key similar to Base64 encoded key if you're using
	// deprecated overload of setSigningKey()
	// this key *MUST* be a valid key for the signature algorithm found in the JWT header
	// private final String SECRET_KEY = "mypersonalkeywhichshouldbereallyreallyreallylong";

	// prefer generating secure random secret key instead of a raw one

	// visit: https://github.com/jwtk/jjwt#jws-create-key and
	// https://github.com/jwtk/jjwt#jws-key-create-secret to learn more

	// generate secret ky
	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	// save the secret key if needed

	// using Encoders from io.jsonwebtoken.io.Encoders (saves the trouble of deciding and using Charset)
	String secretString = Encoders.BASE64.encode(key.getEncoded());
	// in general you don't need to perform this step unless you're
	// transferring (storing) the key for some purpose

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
		// Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		return Jwts.parserBuilder()
			// JWS = signature(JWT)
			// consumes JWS
			.setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString)))
			.build()
			.parseClaimsJws(token).getBody();
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

	// produce the JWS
	private String createToken(Map<String, Object> claims, String subject)
	{
		return Jwts.builder().setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60 * 10))  // ~10 hours
			// this overload of signWith() here is deprecated
			// .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

			// using signWith(Key)
			// hmacShaKeyFor will create a new secret key instance (you don't
			// need to use decoder if you haven't encoded the secret key)
			// you can also use overload signWith(Key, SignatureAlgorithm) to
			// manually provide algorithm but beware of compatibility of
			// algorithm with (generated) secret key
			.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString))).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails)
	{
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
