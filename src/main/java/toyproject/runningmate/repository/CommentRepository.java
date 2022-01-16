package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.runningmate.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
