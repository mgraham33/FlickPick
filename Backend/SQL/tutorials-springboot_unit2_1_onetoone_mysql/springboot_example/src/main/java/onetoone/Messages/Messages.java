package onetoone.Messages;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @Author: Chris Costa
 */
@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the message that was sent", name="id", required=true)
    private int id;
    @ApiModelProperty(notes = "ID of the person who sent the message", name="senderid", required=true)
    private int senderid;
    @ApiModelProperty(notes = "ID of the reciever getting the message", name="receiverid", required=true)
    private int receiverid;
    @ApiModelProperty(notes = "ID of the friendship between the message", name="friendshipid", required=true)
    private int friendshipid;
    @ApiModelProperty(notes = "content of the message that was sent", name="message", required=true)
    private String message;

    /**
     *
     * @param id
     * @param senderid
     * @param receiverid
     * @param friendshipid
     * @param message
     */
    public Messages(int senderid, int receiverid, int friendshipid, String message) {
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.friendshipid = friendshipid;
        this.message = message;
    }

    /**
     * Default contructor
     */
    public Messages() {
    }

    /**
     * Gets the id of the message
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the message
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the id of the sender
     * @return senderid
     */
    public int getSenderid() {
        return senderid;
    }

    /**
     * Sets the id of the sender
     * @param senderid
     */
    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    /**
     * Gets the id of the receiver
     * @return receiverid
     */
    public int getReceiverid() {
        return receiverid;
    }

    /**
     * Sets the id of the receiver
     * @param receiverid
     */
    public void setReceiverid(int receiverid) {
        this.receiverid = receiverid;
    }

    /**
     * gets the friendship id
     * @return friendshipid
     */
    public int getFriendshipid() {
        return friendshipid;
    }

    /**
     * Sets the id of the friendship
     * @param friendshipid
     */
    public void setFriendshipid(int friendshipid) {
        this.friendshipid = friendshipid;
    }

    /**
     * Gets the message betweeen the users
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message to be sent
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
