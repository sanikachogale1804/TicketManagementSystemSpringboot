package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private String secretKey;

    public JWTService() {
        try {
            // Generate a secret key for HmacSHA256 algorithm
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating the key", e);
        }
    }

    public String generateTokenWithRoles(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username) // ✅ Username as subject
                .claim("roles", String.join(",", roles))  // 🔹 Set ko String me convert karo (comma-separated)
                .setIssuedAt(new Date()) // ✅ Issue Date
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // ✅ Expiry: 1 day
                .signWith(getKey(), SignatureAlgorithm.HS512)  // ✅ Correct signing method
                .compact();  // ✅ Generate token
    }

    
    // Generate JWT token (without roles)
    public String generateToken(String username, int userId, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("id", userId);  // ✅ Now using int userId
        claims.put("roles", String.join(",", roles));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 Hour Expiry
                .signWith(getKey())
                .compact();
    }


    // Generate the SecretKey from the encoded secret key
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validate the token by checking its expiration
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;  // If token is invalid, return false
        }
    }

    // Extract username from the token
    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Extract any claim from the token
    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate token with user details
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public List<String> extractRoles(String token) {
        String rolesStr = extractClaims(token, claims -> (String) claims.get("roles"));  
        return List.of(rolesStr.split(","));  // ✅ Comma-separated roles ko List me convert karo
    }

}
