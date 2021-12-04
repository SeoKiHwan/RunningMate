package toyproject.runningmate.repository;

<<<<<<< HEAD



public class CrewRepository {
=======
import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<CrewDto> findBycrewName(String crewName);

>>>>>>> ced1f6fefccd6efcc90eae7926e6dbf4abab98c2
}
