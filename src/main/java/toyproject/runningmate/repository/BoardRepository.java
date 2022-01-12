package toyproject.runningmate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.runningmate.domain.Board.Board;
import toyproject.runningmate.domain.crew.Crew;

import javax.persistence.EntityManager;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
