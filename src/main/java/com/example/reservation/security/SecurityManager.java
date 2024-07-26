package com.example.reservation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityManager {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Value("${token.key}")
  private String tokenKey;

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

  //로그인 필요한 서비스 접근시 헤더에 토큰 포함해서 보내면, 파싱해서 LoginId(subject) 반환
  public String parseToken(String token) {
    return Jwts.parser().setSigningKey(tokenKey)
        .parseClaimsJws(token.replace("partner ", "").replace("user ", ""))
        .getBody().getSubject();
  }

  //로그인 성공시 Jwt 토큰 생성해서 반환
  public String tokenCreate(String loginId, String role) {
    Claims claims = Jwts.claims().setSubject(loginId);
    claims.put("role", role);

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    String token = Jwts.builder().setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS512, tokenKey)
        .compact();

    if ("ROLE_PARTNER".equals(role)) {
      return "partner " + token;
    } else if ("ROLE_USER".equals(role)) {
      return "user " + token;
    }

    return null;
  }

  //로그인시 비밀번호 일치여부 확인
  public boolean passCheck(String rawPassword, String encodedPassword) {
    return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
  }
}
