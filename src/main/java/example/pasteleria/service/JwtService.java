package example.pasteleria.service;

import example.pasteleria.enums.Rol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key KEY = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    private static final long EXPIRATION = 80000000;

    public String generarToken(String username, Rol rol) {
        return Jwts.builder()
                .setClaims(Map.of("rol", rol.name()))
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerDatos(token).getSubject();
    }

    public String extraerRol(String token) {
        return extraerDatos(token).get("rol", String.class);
    }

    private Claims extraerDatos(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
