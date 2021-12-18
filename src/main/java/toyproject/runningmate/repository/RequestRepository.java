package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.runningmate.domain.request.RequestUserToCrew;

public interface RequestRepository extends JpaRepository<RequestUserToCrew,Long> {

}
