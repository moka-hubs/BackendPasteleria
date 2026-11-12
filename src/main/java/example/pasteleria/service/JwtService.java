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

    private final Key key;
    private static final long EXPIRATION = 86_400_000L; // 24 horas

    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        this.key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    }

    public String generarToken(String username, Rol rol) {
        return Jwts.builder()
                .setClaims(Map.of("rol", rol.name()))
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerDatos(token).getSubject();
    }

    public String extraerRol(String token) {
        return extraerDatos(token).get("rol", String.class);
    }

    public long getExpiracion() {
        return EXPIRATION;
    }

    private Claims extraerDatos(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
