package univ.yesummit.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import univ.yesummit.domain.member.service.MemberService;
import univ.yesummit.global.auth.util.JwtUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2Member oAuth2Member = (OAuth2Member) authentication.getPrincipal();
        Long memberId = oAuth2Member.getMemberId();
        log.info("userId: {}", memberId);

        // 첫 로그인 여부 확인
        String redirectUrl = "http://localhost:3000/login-success";

        // 리다이렉트 URL에 userId 추가
        response.sendRedirect(redirectUrl + "?userId=" + memberId);
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
//        // Refresh 토큰을 멤버 엔티티에 저장
//        try {
//            memberService.updateRefreshToken(memberId, refreshToken);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        // 첫 로그인 여부 확인
//        boolean isFirstLogin = memberService.isFirstLogin(memberId);
//
//        // JSON 응답으로 전달할 데이터 생성
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("accessToken", accessToken);
//        responseData.put("refreshToken", refreshToken);
//        responseData.put("firstLogin", isFirstLogin);
//
//        // JSON 응답 설정
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // JSON 데이터를 응답으로 전송
//        new ObjectMapper().writeValue(response.getWriter(), responseData);
//    }
}