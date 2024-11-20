package univ.yesummit.global.oauth.dto;

import lombok.RequiredArgsConstructor;
import univ.yesummit.domain.member.entity.Authority;
import univ.yesummit.domain.member.entity.Member;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    /**
     * 반환값 "kakao"
     * 인증 제공자를 식별, 이 값은 추후 멀티 OAuth2 제공자(Google, Naver 등)
     * 을 구현할 때 사용됨
     */
    @Override
    public String getProvider() {
        return "kakao";
    }

    /**
     * Kakao 사용자 고유 ID 반환
     * attributes 맵에서 "id" 키에 해당하는 값을 가져옴
     * KakaoResponse는 이 고유 ID를 providerId로 사용
     * 이 값은 해당 사용자를 Kakao 서비스 내에서 유일하게 식별
     */
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    /**
     * attributes에서 "kakao_account"라는 키로 사용자 계정 정보를 가져옴
     * 이 값은 다시 Map 형태로 "email"키로 이메일을 추출
     * Kakao 계정 설정에서 이메일 제공이 허용되지 않으면 null이 반환
     */
    @Override
    public String getEmail() {
        return (String) ((Map<?, ?>) attributes.get("kakao_account")).get("email");
    }

    /**
     * attributes에서 "properties" 키로 사용자 프로필 정보를 가져옴
     * Map 형태로 "nickname" 키를 통해 닉네임을 추출
     */
    @Override
    public String getName() {
        return (String) ((Map<?, ?>) attributes.get("properties")).get("nickname");
    }

    /**
     * 소셜 로그인을 통해 받은 사용자 정보를 기반으로 애플리케이션의 Member 엔티티를 생성
     * 이후 이 엔티티는 데이터베이스에 저장
     */
    @Override
    public Member toEntity() {
        return Member.builder()
                .provider(getProvider())
                .providerId(getProviderId())
                .email(getEmail())
                .username(getName())
                .authority(Authority.USER)
                .build();
    }
}
