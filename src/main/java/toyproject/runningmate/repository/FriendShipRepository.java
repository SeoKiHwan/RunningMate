package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.runningmate.domain.friend.FriendShip;

import java.util.Optional;


@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    Optional<FriendShip> findById(Long id);
}
