package toyproject.runningmate.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.dto.CrewDto;
import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findByCrewName(String crewName);
}
