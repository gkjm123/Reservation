package com.example.reservation.security;

import com.example.reservation.exception.ErrorCode;
import com.example.reservation.exception.ServiceException;
import com.example.reservation.repository.PartnerRepository;
import com.example.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final PartnerRepository partnerRepository;
  private final UserRepository userRepository;
  private final SecurityManager securityManager;

  @Override
  public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
    //토큰값 받아 파싱해서 로그인 아이디 찾기
    String loginId = securityManager.parseToken(token);

    //토큰 프리픽스에 따라 파트너 또는 개인회원 Repository 에서 UserDetails(파트너/유저 엔티티) 찾아 반환
    if (token.startsWith("partner ")) {
      return partnerRepository.findByLoginId(loginId)
          .orElseThrow(() -> new ServiceException(ErrorCode.PARTNER_NOT_FOUND));
    } else if (token.startsWith("user ")) {
      return userRepository.findByLoginId(loginId)
          .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUNT));
    }
    return null;
  }

  //토큰 받아서 loadUserByUsername 을 통해 UserDetails 찾고 Authentication 객체 만들어 반환
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = loadUserByUsername(token);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
