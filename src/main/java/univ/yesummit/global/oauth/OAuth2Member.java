package univ.yesummit.global.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import univ.yesummit.domain.member.entity.Authority;
import univ.yesummit.domain.member.entity.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record OAuth2Member(Member member) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(Authority.USER.name()));
    }

    @Override
    public String getName() {
        return member.getUsername();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public static OAuth2Member from(Member member) {
        return new OAuth2Member(member);
    }
}
