package univ.yesummit.global.oauth.dto;

import univ.yesummit.domain.member.entity.Member;

public interface OAuth2Response {

    // 제공자 (naver, google, kakao 등)
    String getProvider();

    // 제공자에서 발급해주는 아이디
    String getProviderId();

    // 사용자 정보
    String getEmail();

    String getName();

    Member toEntity();
}