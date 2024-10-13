package com.example.DATN_WebFiveTus.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtTokenProvider {

    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private int expirationMs;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.headerString}")
    private String headerString;

//    Tạo token:
public String generateToken(Authentication authentication) {
    String username = authentication.getName();
    List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + expirationMs);

    String token = Jwts.builder()
            .setHeaderParam("typ", "JWT") // T
            .setSubject(username)
            .claim("roles", roles) // Thêm vai trò vào token
            .setIssuedAt(currentDate)
            .setExpiration(expireDate)
            .signWith(key())
            .compact();

    return token;
}

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class); // Trả về danh sách vai trò
    }



    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
    }


//    Trích xuất thông tin:
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        return username;
    }

//    Xác thực token:
public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token); // Phải dùng parseClaimsJws để kiểm tra chữ ký
        return true;
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
        logger.error("JWT token expired: {}", e.getMessage());
    } catch (io.jsonwebtoken.SignatureException e) {
        logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (io.jsonwebtoken.MalformedJwtException e) {
        logger.error("Malformed JWT token: {}", e.getMessage());
    } catch (Exception e) {
        logger.error("Invalid JWT token: {}", e.getMessage());
    }
    return false;
}


}
