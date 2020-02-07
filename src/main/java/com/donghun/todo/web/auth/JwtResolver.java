package com.donghun.todo.web.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtResolver {

    @Value("${todo.jjwt.secret}")
    private String secret;

    public boolean isTokenExist(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token == null;
    }

    public String getUserByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token);

        return (String) parsedToken.getBody().get("idx");
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Boolean validateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
    }
}
