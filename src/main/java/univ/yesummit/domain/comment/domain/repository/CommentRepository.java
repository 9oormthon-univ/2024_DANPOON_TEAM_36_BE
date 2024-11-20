package univ.yesummit.domain.comment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
