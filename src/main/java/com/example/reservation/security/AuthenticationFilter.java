package com.example.reservation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String TOKEN_HEADER = "TOKEN";
  private final SecurityManager securityManager;
  private final MemberService memberService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    //Request 의 헤더에서 토큰값을 받아와서 유효성 체크후 SecurityContextHolder 세팅
    String token = request.getHeader(TOKEN_HEADER);
    if (token != null && securityManager.parseToken(token) != null) {
      SecurityContextHolder.getContext().setAuthentication(memberService.getAuthentication(token));
    }
    filterChain.doFilter(request, response);
  }
}
