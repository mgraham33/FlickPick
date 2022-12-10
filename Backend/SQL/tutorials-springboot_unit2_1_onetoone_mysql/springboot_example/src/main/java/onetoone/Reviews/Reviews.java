package onetoone.Reviews;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Author: Chris Costa and Matt Graham
 */

@Entity
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the review", name = "id", required = true)
    private int id;
    @ApiModelProperty(notes = "Rating of the movie", name = "rating", required = true)
    private double rating;
    @ApiModelProperty(notes = "ID of the movie for the review", name = "movieid", required = true)
    private int movieid;
    @ApiModelProperty(notes = "ID of the user who made the review", name = "userid", required = true)
    private int userid;
    @ApiModelProperty(notes = "Reason for the review", name = "reason", required = true)
    private String reason;

    /**
     *
     * @param movieid referenced movie id
     * @param userid author user id
     * @param rating rating
     * @param reason description
     */
    public Reviews(int movieid, int userid, double rating, String reason) {
        this.movieid = movieid;
        this.userid = userid;
        this.rating = rating;
        this.reason = reason;
    }

    /**
     * default constructor
     */
    public Reviews() {
    }

    /**
     * gets the id of the review
     * @return review id
     */
    public int getId(){
        return id;
    }

    /**
     * sets the id of the review
     * @param id review id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the rating
     * @return rating
     */
    public double getRating() { return rating; }

    /**
     * sets the rating
     * @param rating rating
     */
    public void setRating(double rating) { this.rating = rating; }

    /**
     * gets the referenced movie id
     * @return referenced movie id
     */
    public int getMovieid() { return movieid; }

    /**
     * sets the referenced movie id
     * @param movieid referenced movie id
     */
    public void setMovieid(int movieid) { this.movieid = movieid; }

    /**
     * gets the author user id
     * @return author user id
     */
    public int getUserid() { return userid; }

    /**
     * sets the author user id
     * @param userid author user id
     */
    public void setUserid(int userid) { this.userid = userid; }

    /**
     * gets description
     * @return review description
     */
    public String getReason(){
        return reason;
    }

    /**
     * sets the description
     * @param reason review description
     */
    public void setReason(String reason){
        this.reason = reason;
    }


}

