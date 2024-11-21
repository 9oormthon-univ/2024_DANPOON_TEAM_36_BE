package univ.yesummit.global.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import univ.yesummit.domain.member.service.MemberService;
import univ.yesummit.global.auth.util.JwtUtils;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2SuccessHandler.onAuthenticationSuccess Member Name : {}", authentication.getName());

        OAuth2Member oAuth2Member = (OAuth2Member) authentication.getPrincipal();
        Long memberId = oAuth2Member.getMemberId();

        // JWT 토큰 생성
        String accessToken = jwtUtils.createAccessToken(memberId);
        String refreshToken = jwtUtils.createRefreshToken(memberId);

        // Refresh 토큰을 멤버 엔티티에 저장
        try {
            memberService.updateRefreshToken(memberId, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 토큰을 쿠키에 저장
        int accessTokenMaxAge = jwtUtils.getAccessExpiration().intValue() / 1000; // 밀리초를 초로 변환
        int refreshTokenMaxAge = jwtUtils.getRefreshExpiration().intValue() / 1000;

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(accessTokenMaxAge);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 첫 로그인 여부에 따라 리다이렉트
        if (memberService.isFirstLogin(memberId)) {
            response.sendRedirect("localhost:3000/additional-info");
        } else {
            response.sendRedirect("localhost:3000/home");
        }
    }
}


//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.info("OAuth2SuccessHandler.onAuthenticationSuccess Member Name : {}", authentication.getName());
//
//        OAuth2Member oAuth2Member = (OAuth2Member) authentication.getPrincipal();
//        Long memberId = oAuth2Member.getMemberId();
//
//        // JWT 토큰 생성
//        String accessToken = jwtUtils.createAccessToken(memberId);
//        String refreshToken = jwtUtils.createRefreshToken(memberId);
//
//        // RefreshToken을 DB에 저장
//        try {
//            memberService.updateRefreshToken(memberId, refreshToken);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        // 클라이언트에 JWT 토큰 전송
//        jwtUtils.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//
//        // 첫 로그인 여부에 따라 리다이렉트
//        if (memberService.isFirstLogin(memberId)) {
//            response.sendRedirect("localhost:3000/additional-info");
//        } else {
//            response.sendRedirect("localhost:3000/home");
//        }
//    }
//}
//
