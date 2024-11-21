package univ.yesummit.domain.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.board.domain.Board;


public interface BoardRepository extends JpaRepository<Board, Long> {


}
