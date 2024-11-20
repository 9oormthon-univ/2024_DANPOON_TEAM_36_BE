package univ.yesummit.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.global.exception.ErrorCode;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByUsernameOrThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    boolean existsByUsername(String username);
}
