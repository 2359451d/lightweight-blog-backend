package top.bento.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    //server secret to encrypt header & payload
    private static final String jwtKey = "123456Mszlu!@#$$";

    public static String createToken(Long userId) {
        // payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtKey) // issue algo, jwtToken
                .setClaims(claims) // unique payload
                .setIssuedAt(new Date()) // issue time - ensure token uniqueness
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));// token valid period - 24h
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            // using secret key to decrypt the token
            Jwt parse = Jwts.parser().setSigningKey(jwtKey).parse(token);
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
