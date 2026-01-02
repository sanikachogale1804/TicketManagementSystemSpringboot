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

import com.example.Demo.TicketManagementSystemCogent_1.DTO.UserTokenDTO;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;

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
    public String generateToken(User user) {
        // Create a simplified DTO instead of passing the whole User entity
        UserTokenDTO userTokenDTO = new UserTokenDTO(user.getUserId(), user.getUserName(), user.getRole().toString());

        // Generate JWT token based on this simplified DTO
        String token = generateToken(userTokenDTO);  // Use the DTO to generate token
        return token;
    }

    public String generateToken(UserTokenDTO userTokenDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userTokenDTO.getUserName());
        claims.put("id", userTokenDTO.getUserId());
        claims.put("roles", userTokenDTO.getRole()); // Ensure this is correctly passed as a string

        return Jwts.builder()
                .setClaims(claims) // Set claims
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // Set expiration time (1 hour)
                .signWith(getKey(), SignatureAlgorithm.HS256) // Sign with secret key
                .compact(); // Return compact JWT token
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

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");

        if (roles instanceof String) {
            return List.of((String) roles);
        }
        return (List<String>) roles;
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



}
