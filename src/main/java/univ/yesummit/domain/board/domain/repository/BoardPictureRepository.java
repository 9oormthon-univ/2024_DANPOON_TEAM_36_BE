package univ.yesummit.domain.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardPicture;

import java.util.List;

public interface BoardPictureRepository extends JpaRepository<BoardPicture, String> {

    void deleteByBoardBoardId(Long boardId);

    void deleteByBoardAndImageUrlIn(Board board, List<String> urlsToDelete);
}
