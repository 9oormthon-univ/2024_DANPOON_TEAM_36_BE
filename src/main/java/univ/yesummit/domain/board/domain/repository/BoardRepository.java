package univ.yesummit.domain.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.board.domain.Board;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBySummitId(Long summitId);
}
