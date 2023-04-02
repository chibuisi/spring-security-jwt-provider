package com.chibuisi.springsecapp.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtil {
	private Logger log = Logger.getLogger("HomeResource");
	@Value("${app.key_token}")
	private String SECRET_KEY;
//	@Value("${app.jwtExpirationMs}")
//	private String JWT_EXPIRATION_MS;

	private static final Logger logger = Logger.getLogger(JwtUtil.class.getSimpleName());

	private String createToken(Map<String,Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 43200000))
				.setIssuer("api.minor-insights.com").signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
//		claims.put("auth", userDetails.getAuthorities().stream()
//				.map(s -> new SimpleGrantedAuthority(s.getAuthority()))
//				.filter(Objects::nonNull).collect(Collectors.toList()));
		String [] authorities = userDetails.getAuthorities().stream().map(e -> e.getAuthority()).toArray(String[]::new);
		claims.put("authorities", authorities);
		return createToken(claims, userDetails.getUsername());
	}
	
	private Claims extractAllClaims(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
			return claims;
		}
		catch (SignatureException e) {
			logger.log(Level.parse("Invalid JWT signature: {}"), e.getMessage());
		} catch (MalformedJwtException e) {
			logger.log(Level.parse("Invalid JWT token: {}"), e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.log(Level.parse("JWT token is expired: {}"), e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.log(Level.parse("JWT token is unsupported: {}"), e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.log(Level.parse("JWT claims string is empty: {}"), e.getMessage());
		}
		return null;
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
