package com.chibuisi.springsecapp.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtUtil {
	private Logger log = Logger.getLogger("HomeResource");
	@Value("${key_token}")
	private String SECRET_KEY;
	
	  @PostConstruct
	  protected void init() {
		  SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
		  byte[] key = Base64.getDecoder().decode(SECRET_KEY.getBytes());
		  String value = new String(key, StandardCharsets.UTF_8);
	  }
	
	private String createToken(Map<String,Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.setIssuer("chibuisi").signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
//		claims.put("auth", userDetails.getAuthorities().stream()
//				.map(s -> new SimpleGrantedAuthority(s.getAuthority()))
//				.filter(Objects::nonNull).collect(Collectors.toList()));
		String [] authorities = userDetails.getAuthorities().stream().map(e -> e.getAuthority()).toArray(String[]::new);
		claims.put("auth", authorities);
		return createToken(claims, userDetails.getUsername());
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Date extractExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Boolean hasExpired(String token) {
		return extractExpirationDate(token).before(new Date());
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !hasExpired(token);
	}

	public Boolean verify(String token){
	  	try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
		}
		catch (Exception e){
			log.info("JWT verification is failed: "+ e.getMessage());
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
	  	return Boolean.TRUE;
	}
}
