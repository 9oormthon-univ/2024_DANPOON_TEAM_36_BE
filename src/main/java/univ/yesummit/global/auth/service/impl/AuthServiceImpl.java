package univ.yesummit.global.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.member.service.MemberService;
import univ.yesummit.global.auth.service.AuthService;
import univ.yesummit.global.auth.util.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    public Map<String, Object> generateTokens(Long userId) {
        // JWT 토큰 생성
        String accessToken = jwtUtils.createAccessToken(userId);
        String refreshToken = jwtUtils.createRefreshToken(userId);

        // 첫 로그인 여부 확인
        boolean isFirstLogin = memberService.isFirstLogin(userId);

        // 결과 데이터 구성
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("firstLogin", isFirstLogin);

        return tokens;
    }
}
