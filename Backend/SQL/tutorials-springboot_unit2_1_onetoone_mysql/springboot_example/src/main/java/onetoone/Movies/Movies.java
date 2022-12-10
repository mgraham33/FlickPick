package onetoone.Movies;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import io.swagger.annotations.ApiModelProperty;

/**
 * Author: Chris Costa and Matt Graham
 */

@Entity
public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the Movie", name="id", required=true)
    private int id;
    @ApiModelProperty(notes = "Title of the Movie", name="title", required=true)
    private String title;
    @ApiModelProperty(notes = "Duration of the Movie", name="minutes", required=true)
    private int minutes;
    @ApiModelProperty(notes = "Release year of the Movie", name="year", required=true)
    private int year;
    @ApiModelProperty(notes = "Rating of the Movie", name="rating", required=true)
    private double rating;
    @ApiModelProperty(notes = "Description of the Movie", name="description", required=true)
    private String description;
    @ApiModelProperty(notes = "Thumbnail of the Movie", name="picture", required=true)
    private String picture;

    /**
     *
     * @param title title
     * @param minutes duration in minutes
     * @param year release year
     * @param rating average rating
     * @param description description
     * @param picture thumbnail
     */
    public Movies(String title, int minutes, int year, double rating, String description, String picture) {
        this.title = title;
        this.minutes = minutes;
        this.year = year;
        this.rating = rating;
        this.description = description;
        this.picture = picture;
    }

    /**
     * default constructor
     */
    public Movies() {
    }

    /**
     * gets movie id
     * @return movie id
     */
    public int getId(){
        return id;
    }

    /**
     * sets movie id
     * @param id movie id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * gets title
     * @return title
     */
    public String getTitle(){
        return title;
    }

    /**
     * sets title
     * @param title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * gets duration in minutes
     * @return duration in minutes
     */
    public int getMinutes(){
        return minutes;
    }

    /**
     * sets duration in minutes
     * @param minutes duration in minutes
     */
    public void setMinutes(int minutes){
        this.minutes = minutes;
    }

    /**
     * gets release year
     * @return release year
     */
    public int getYear(){
        return year;
    }

    /**
     * sets release year
     * @param year release year
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * gets average rating
     * @return average rating
     */
    public double getRating(){
        return rating;
    }

    /**
     * sets average rating
     * @param rating sets average rating
     */
    public void setRating(double rating){
        this.rating = rating;
    }

    /**
     * gets description
     * @return description
     */
    public String getDescription(){
        return description;
    }

    /**
     * sets description
     * @param description description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * gets thumbnail
     * @return thumbnail
     */
    public String getPicture(){
        return picture;
    }

    /**
     * sets thumbnail
     * @param picture thumbnail
     */
    public void setPicture(String picture){
        this.picture = picture;
    }

}
