package com.employee.security.util;

import com.employee.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        List<String> roles = user.getRoles().stream().map(r -> "ROLE_" + r.name()).toList();
        List<String> permissions = user.getPermissions().stream().map(Enum::name).toList();
        List<String> authority = user.getAuthority().stream().map(Enum::name).toList();
        return Jwts
                .builder()
                .subject(user.getUsername())
                .claim("userId",user.getUserId().toString())
                .claim("roles",roles)
                .claim("permissions",permissions)
                .claim("authority",authority)
                .issuedAt(new Date())
                .signWith(getSecretKey())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .compact();
    }

    public String getUserFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
