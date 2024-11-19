package univ.yesummit.global.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.global.oauth.dto.KakaoResponse;
import univ.yesummit.global.oauth.dto.OAuth2Response;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberService extends DefaultOAuth2UserService { //DefaultOAuth2UserService를 상속받아 기본 OAuth2 사용자 정보를 로드하는 기능을 확장

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); //OAuth2 서버에서 사용자 정보를 가져옴
        log.info("memberInfo = {}", oAuth2User.getAttributes());

        OAuth2Response oAuth2Response = getOAuth2Response(userRequest, oAuth2User)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        "Unsupported provider: " + userRequest.getClientRegistration().getRegistrationId()));

        Member member = getOrSave(oAuth2Response);

        return OAuth2Member.from(member);
    }

    // 소셜 로그인 제공자(Kakao, Google) 구분
    private Optional<OAuth2Response> getOAuth2Response(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        if ("kakao".equals(registrationId)) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        return Optional.of(oAuth2Response);
    }

    /**
     *  provider와 providerId를 기준으로 데이터베이스에서 사용자를 조회
     * @param oAuth2Response
     * @return
     */
    private Member getOrSave(OAuth2Response oAuth2Response) {
        Optional<Member> optionalMember = memberRepository.findByProviderAndProviderId(oAuth2Response.getProvider(), oAuth2Response.getProviderId());

        if (optionalMember.isPresent()) {
            return optionalMember.get();
        } else {
            Member member = oAuth2Response.toEntity();
            return memberRepository.save(member);
        }
    }
}