package univ.yesummit.global.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.domain.member.service.MemberService;
import univ.yesummit.global.auth.service.AuthService;
import univ.yesummit.global.auth.util.JwtUtils;
import univ.yesummit.global.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final JwtUtils jwtUtils;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public Map<String, Object> generateTokens(Long userId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));


        // JWT 토큰 생성
        String accessToken = jwtUtils.createAccessToken(userId);
        String refreshToken = jwtUtils.createRefreshToken(userId);

        member.updateRefreshToken(refreshToken);

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
