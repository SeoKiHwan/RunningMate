package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.runningmate.domain.friend.FriendShip;

import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    Optional<FriendShip> findById(Long id);
}
