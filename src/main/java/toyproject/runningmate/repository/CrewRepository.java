package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findBycrewName(String crewName);

}
