package univ.yesummit.domain.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardLike;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByBoardAndMember(Board board, Member member);

    Optional<BoardLike> findByBoardAndMember(Board board, Member member);

    List<BoardLike> findByMember(Member member);

}
