package univ.yesummit.global.oauth;

import jakarta.servlet.ServletException;
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

        // JWT 토큰 발급
        String accessToken = jwtUtils.createAccessToken(memberId);
        String refreshToken = jwtUtils.createRefreshToken(memberId);

        // refreshToken을 member 엔티티에 저장
        try {
            memberService.updateRefreshToken(memberId, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        jwtUtils.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // 기존 회원인지 확인
        if (memberService.isFirstLogin(memberId)) {
            // 첫 로그인 시 추가 정보 입력 페이지로 리다이렉트
            response.sendRedirect("/additional-info");
            return;
        }

        // 기존 회원이라면 정상 응답 (e.g., 메인 페이지로 리다이렉트)
        response.sendRedirect("/home");
    }
}
