package com.example.androidserver.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    // 비밀키
    private static final String SECRET_KEY = "ymSND8rq83j4fmJZj79wh4OrMNuntIj4fmJ";
    // 토큰 유효기간 설정
    private static final long EXPIRATION_TIME = 60*60*24*30*1000L; // 30 days

    // jwt 토큰 생성기
    public static String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // 페이로드 부분 맵 추가
        Claims claims = Jwts.claims();
        claims.put("userEmail", email);
        // Create header with "typ" set to "JWT"
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");

        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(createKey())
                .compact();
    }

    // 토큰 검증
    public static Claims checkJwt(String jwt) throws Exception {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(createKey())
                    .build()
                    // parseClaimsJws에서 해독과 함께 jwt를 검증하게 되고,
                    // 만료시간이 다 되었거나, jwt가 유효하지 않다면 Exception을 던지게 됨.
                    .parseClaimsJws(jwt)
                    .getBody();
            System.out.println("email : " + claims.get("userEmail"));
        } catch (ExpiredJwtException e) {
            System.out.println("ExpiredJwtException 발생");
            e.printStackTrace();
            return null;
        } catch (JwtException e) {
            System.out.println("JwtException 발생");
            e.printStackTrace();
            return null;
        }
        return claims;
    }

    // 키 생성
    private static Key createKey() {
        // 비밀키를 바이트로 변환
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        // 암호화 및 서명 작업에 사용할 키를 생성하는 메서드
        // 서버키를 활용하여 HMAC 알고리즘 기반한 Key 객체를 생성함
        return Keys.hmacShaKeyFor(keyBytes);
    }
}