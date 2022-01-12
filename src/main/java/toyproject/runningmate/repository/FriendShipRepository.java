package toyproject.runningmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.friend.FriendShip;
import toyproject.runningmate.domain.user.User;

import java.util.Optional;


@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {


    @Query(value = "delete from FriendShip f where f.sendUser in :user or f.receiveUserNickName in :name")
    @Modifying
    @Transactional
    public void deleteUserAllFriendShip(
            @Param("user") User user,
            @Param("name") String name
    );

    Optional<FriendShip> findById(Long id);
}
