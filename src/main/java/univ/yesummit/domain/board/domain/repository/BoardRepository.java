package univ.yesummit.domain.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findFirstByWriter(Member member);
    List<Board> findBySummitId(Long summitId);
}
