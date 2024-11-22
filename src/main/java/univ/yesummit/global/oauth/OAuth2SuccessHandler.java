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

        // 첫 로그인 여부 확인
        boolean firstLogin = memberService.isFirstLogin(memberId);

        // 응답 데이터 생성
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("firstLogin", firstLogin);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 생성
        new ObjectMapper().writeValue(response.getWriter(), tokens);
    }
}

//        // 토큰을 쿠키에 저장
//        int accessTokenMaxAge = jwtUtils.getAccessExpiration().intValue() / 1000; // 밀리초를 초로 변환
//        int refreshTokenMaxAge = jwtUtils.getRefreshExpiration().intValue() / 1000;

//        // Access Token 쿠키
//        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
//                .httpOnly(true)
//                .secure(true) // HTTPS에서만 동작
//                .sameSite("None") // Cross-Domain 허용
//                .path("/")
//                .domain("yesummit.kro.kr") // 도메인 지정
//                .maxAge(accessTokenMaxAge)
//                .build();
//
//        // Refresh Token 쿠키
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(true)
//                .sameSite("None")
//                .path("/")
//                .domain("yesummit.kro.kr")
//                .maxAge(refreshTokenMaxAge)
//                .build();
//
//
//        response.addHeader("Set-Cookie", accessTokenCookie.toString());
//        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // 첫 로그인 여부에 따라 리다이렉트
//        if (memberService.isFirstLogin(memberId)) {
//            response.sendRedirect("http://localhost:3000/signup");
//        } else {
//            response.sendRedirect("http://localhost:3000/home");
//        }
//    }
//}