package univ.yesummit.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import univ.yesummit.domain.feed.entity.Feed;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findAllByMember(Member member);
}
