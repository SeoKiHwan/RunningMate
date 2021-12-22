package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestUserToCrew, Long> {
    Optional<RequestUserToCrew> findByNickName(String nickName);
}
