package onetoone.Friends;

import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * Author: Chris Costa and Matt Graham
 */

@Entity
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the friendship", name = "id", required = true)
    private int id;
    @ApiModelProperty(notes = "id of the first user in the friendship", name = "user1", required = true)
    private int user1;
    @ApiModelProperty(notes = "id of the second user in the friendship", name = "user2", required = true)
    private int user2;
    @ApiModelProperty(notes = "Type of friend (accepted, requested, blocked or unblocked and rejected)", name = "friend_type", required = true)
    private String friend_type;

    /**
     *
     * @param user1 first user id
     * @param user2 second user id
     * @param friend_type friendship type
     */
    public Friends(int user1, int user2, String friend_type) {
        this.user1 = user1;
        this.user2 = user2;
        this.friend_type = friend_type;
    }

    /**
     * default constructor
     */
    public Friends() {
    }

    /**
     * gets the id of the friendship
     * @return id of friendship
     */
    public int getId() {return id;}

    /**
     * sets the id of the friendship
     * @param id id of friendship
     */
    public void setId(int id) {this.id = id;}

    /**
     * gets the id of the first user
     * @return id of the first user
     */
    public int getUser1(){
        return user1;
    }

    /**
     * sets id of first user of friendship
     * @param user1 id of first user
     */
    public void setUser1(int user1){
        this.user1 = user1;
    }

    /**
     * gets id of second user of friendship
     * @return id of second user
     */
    public int getUser2(){
        return user2;
    }

    /**
     * sets id of second user of friendship
     * @param user2 id of second user
     */
    public void setUser2(int user2){
        this.user2 = user2;
    }

    /**
     * gets friend type of friendship
     * @return friend type of friendship
     */
    public String getFriendtype(){
        return friend_type;
    }

    /**
     * sets friend type of friendship
     * @param friend_type friend type (accepted, rejected, blocked, or requested)
     */
    public void setFriend_type(String friend_type){
        this.friend_type = friend_type;
    }

}
