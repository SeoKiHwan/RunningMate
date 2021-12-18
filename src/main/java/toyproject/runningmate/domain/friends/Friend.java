package toyproject.runningmate.domain.friends;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.user.User;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User fromUser;

    @Column(name = "TO_USER_ID")
    private Long toUserID;

    public void setUser(User user){
        this.fromUser=user;
        if(!user.getFriends().contains(this)){
            user.getFriends().add(this);
        }
    }
}
