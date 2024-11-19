package univ.yesummit.global.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.member.repository.MemberRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "member_id";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;

    public String createAccessToken(Long memberId) {

        log.info("memberId info: {}", memberId.toString());

        String accessToken = JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration))
                .withClaim(ID_CLAIM, memberId)
                .sign(Algorithm.HMAC512(secret));


        log.info("Generated Access Token: {}", accessToken);

        return accessToken;
    }


    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .sign(Algorithm.HMAC512(secret));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("Access Token: {}, Refresh Token: {}", accessToken, refreshToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(accessHeader);
        log.info("Access header: {}", header);
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<Long> extractMemberId(String token) {
        Long memberId = JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getClaim("member_id")
                .asLong();

        return Optional.ofNullable(memberId);
    }

//    public Map<String, Object> getClaims(String token) {
//        try {
//            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret)) // 동일한 키와 알고리즘 사용
//                    .build()
//                    .verify(token); // 토큰 검증
//
//            // 클레임 반환
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("subject", decodedJWT.getSubject());
//            claims.put("member_id", decodedJWT.getClaim(ID_CLAIM).asLong());
//            claims.put("expiresAt", decodedJWT.getExpiresAt());
//
//            return claims;
//        } catch (Exception e) {
//            log.error("Invalid token: {}", e.getMessage());
//            throw new IllegalArgumentException("Invalid token", e);
//        }
//    }

    //== 추후에 유지보수하는 과정에서 리프레시 토큰의 만료 혹은 토큰의 블랙리스트를 구현할 때 사용할 것 ==//

//
//    public void destroyRefreshToken(String username) {
//        memberRepository.findByUsername(username)
//                .ifPresentOrElse(
//                        Member::destroyRefreshToken,
//                        () -> { throw new MemberException(ErrorCode.NOT_FOUND_MEMBER); }
//                );
//    }
//
//    public void updateRefreshToken(String username, String refreshToken) {
//        memberRepository.findByUsername(username)
//                .ifPresentOrElse(
//                        member -> member.updateRefreshToken(refreshToken),
//                        () -> { throw new MemberException(ErrorCode.NOT_FOUND_MEMBER); }
//                );
//    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("Send AccessToken: {}", accessToken);
    }

    /* 토큰 유효성 검증 */
    public boolean isValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 Token입니다", e.getMessage());
            return false;
        }
    }
    //==========================================================================//
}