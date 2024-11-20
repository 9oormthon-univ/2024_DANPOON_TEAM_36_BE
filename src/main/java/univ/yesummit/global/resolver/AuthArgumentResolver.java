package univ.yesummit.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.global.auth.util.JwtUtils;
import univ.yesummit.global.exception.ErrorCode;

import java.util.Objects;

/**
 * 컨트롤러 메서드의 파라미터에 어노테이션 @User , LoginUser가 사용된 경우
 * 요청의 JWT 토큰에서 사용자 ID를 추출하여 해당 파라미터에 주입
 */
@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    /**
     * 현재 파라미터가 여기 resolver에서 처리할 대상인지 확인
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(User.class)
                && parameter.getParameterType().equals(LoginUser.class);
    }

    /**
     * 요청에서 JWT토큰을 추출하고 해당 토큰에서 사용자 ID를 가져와 LoginUser 객체를 생성 -> 반환
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        // Access Token 추출
        String accessToken = jwtUtils.extractAccessToken(Objects.requireNonNull(request))
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_ACCESS_TOKEN));

        // memberId 추출
        Long memberId = jwtUtils.extractMemberId(accessToken)
                .orElseThrow(() -> new MemberException(ErrorCode.INVALID_ACCESS_TOKEN));

        // LoginUser 객체 반환
        return new LoginUser(memberId);
    }
}
