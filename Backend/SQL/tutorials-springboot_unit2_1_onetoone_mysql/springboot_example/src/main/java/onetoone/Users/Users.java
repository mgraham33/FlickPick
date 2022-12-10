package onetoone.Users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import io.swagger.annotations.ApiModelProperty;

/**
 * Author: Chris Costa and Matt Graham
 */

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the user", name = "id", required = true)
    private int id;
    @ApiModelProperty(notes = "username of the given user", name = "username", required = true)
    private String username;
    @ApiModelProperty(notes = "The email of the given user", name = "email", required = true)
    private String email;
    @ApiModelProperty(notes = "The type of user the user is", name = "user_type", required = true)
    private String user_type;
    @ApiModelProperty(notes = "The password of the user", name = "password", required = true)
    private String password;
    @ApiModelProperty(notes = "The users favorite movieID", name = "movieid", required = true)
    private int movieid;
    @ApiModelProperty(notes = "The users profile picture", name = "pfp", required = true)
    private String pfp;

    /**
     *
     * @param username unique username
     * @param email unique email
     * @param user_type user permission type
     * @param password password
     * @param movieid favorite movie id
     * @param pfp profile picture
     */
    public Users(String username, String email, String user_type, String password, int movieid, String pfp) {
        this.username = username;
        this.email = email;
        this.user_type = user_type;
        this.password = password;
        this.movieid = movieid;
        this.pfp = pfp;
    }

    /**
     * default constructor
     */
    public Users() {
    }

    /**
     * gets user id
     * @return user id
     */
    public int getId(){
        return id;
    }

    /**
     * sets user id
     * @param id user id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * gets username
     * @return username
     */
    public String getUsername(){
        return username;
    }

    /**
     * sets username
     * @param username username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * gets user permission type
     * @return user permission type
     */
    public String getUserType(){
        return user_type;
    }

    /**
     * sets user permission type
     * @param user_type user permission type
     */
    public  void setUserType(String user_type){
        this.user_type = user_type;
    }

    /**
     * gets user email
     * @return email
     */
    public String getEmail(){
        return email;
    }

    /**
     * sets user email
     * @param email email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * gets user password
     * @return password
     */
    public String getPassword(){
        return password;
    }

    /**
     * sets user password
     * @param password password
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * gets favorite movie id
     * @return favorite movie id
     */
    public int getFavMID(){
        return movieid;
    }

    /**
     * sets favorite movie id
     * @param movieid favorite movie id
     */
    public void setFavMID(int movieid){
        this.movieid = movieid;
    }

    /**
     * gets profile picture
     * @return profile picture
     */
    public String getPfp(){
        return pfp;
    }

    /**
     * sets profile picture
     * @param pfp profile picture
     */
    public void setPfp(String pfp){
        this.pfp = pfp;
    }
    
}
