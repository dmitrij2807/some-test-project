package dev.mesh.moneytransfer.service;

import dev.mesh.moneytransfer.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

  @Value("${security.token.signing.key}")
  private String jwtSigningKey;

  public String generateToken(User user) {
    return Jwts.builder()
        .issuedAt(new Date(System.currentTimeMillis()))
        .subject(user.getName())
        .id(user.getId().toString())
        .signWith(signingKey(), SignatureAlgorithm.HS256).compact();
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractUserId(String token) {
    return extractClaim(token, Claims::getId);
  }

  public Long getCurrentUserId() {
    var details = SecurityContextHolder.getContext().getAuthentication().getDetails();
    if (details instanceof Map) {
      return Long.valueOf(((Map<String, String>) details).get("userId"));
    }

    throw new RuntimeException("Incorrect auth data");

  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername()));
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(signingKey()).build().parseClaimsJws(token)
        .getBody();
  }

  private Key signingKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
