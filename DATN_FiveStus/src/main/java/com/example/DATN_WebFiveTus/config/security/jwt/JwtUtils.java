package com.example.DATN_WebFiveTus.config.security.jwt;

import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.service.Imp.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static ConcurrentMap<String, String> blackListToken = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, String> blackListOtp = new ConcurrentHashMap<>();
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        String token;

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String tokeFromBlackList = blackListToken.get(userPrincipal.getUsername());

        if (!ObjectUtils.isEmpty(tokeFromBlackList) && this.validateJwtToken(tokeFromBlackList)) {
            token = tokeFromBlackList;
        } else {
            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            token = Jwts.builder()
                    .setSubject((userPrincipal.getEmail()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .claim("roles", roles)
                    .signWith(key(), SignatureAlgorithm.HS256)
                    .compact();
            blackListToken.put(userPrincipal.getEmail(), token);

        }

        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRolesFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class); // Trả về danh sách các role
    }

    public boolean checkBlackList(String token) {
        return blackListToken.containsKey(getUserNameFromJwtToken(token));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


    public boolean deleteToken(String userName) {
        if (blackListToken.containsKey(userName)) {
            blackListToken.remove(userName);
            return true;
        }
        return false;
    }

    public String generateOtp(String email) {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        String characters = "0123456789";

        for (int i = 0; i < 6; i++) {
            otp.append(characters.charAt(random.nextInt(characters.length())));
        }
        blackListOtp.put(email, otp.toString());
        return otp.toString();
    }

    public boolean fillOtp(String otp, String email) {
        String storedOtp = blackListOtp.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void removeOtp(String email) {
        blackListOtp.remove(email);
    }

}
