package com.hospital.appointmentservice.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final static String SECRET = "secretKey";

    //Create token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();// claims contain additional data for token
        claims.put("role",userDetails.getAuthorities().iterator().next().getAuthority());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())// User name
                .setIssuedAt(new Date())// release time of token
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))//expiration time of token
                .signWith(SignatureAlgorithm.HS256,SECRET)//
                .compact();
    }

    //Analytic token
    public Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    //compare claims in token vs information UserDetails
    public Boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //Valid expired token
    public Boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    //
    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }


}
