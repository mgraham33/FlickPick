package onetoone.Genres;

import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
* Author: Chris Costa and Matt Graham
 */

@Entity
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the Genre set", name="id", required=true)
    private int id;
    @ApiModelProperty(notes = "First Genre name", name="genre1", required=true)
    private String genre1;
    @ApiModelProperty(notes = "Second Genre name", name="genre2", required=true)
    private String genre2;
    @ApiModelProperty(notes = "Third Genre name", name="genre3", required=true)
    private String genre3;

    /**
     *
     * @param genre1 first genre
     * @param genre2 second genre
     * @param genre3 third genre
     */
    public Genres( String genre1, String genre2, String genre3) {
        this.genre1 = genre1;
        this.genre2 = genre2;
        this.genre3 = genre3;
    }

    /**
     * default constructor
     */
    public Genres() {
    }

    /**
     * gets id of genre set
     * @return id of genre set
     */
    public int getId(){
        return id;
    }

    /**
     * sets id of genre set
     * @param id id of genre set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * gets the first genre of the genre set
     * @return the first genre of the genre set
     */
    public String getGenre1(){
        return genre1;
    }

    /**
     * sets the first genre of the genre set
     * @param genre1 first genre of the genre set
     */
    public void setGenre1(String genre1){
        this.genre1 = genre1;
    }

    /**
     * gets the second genre of the genre set
     * @return the second genre of the genre set
     */
    public String getGenre2(){
        return genre2;
    }

    /**
     * sets the second genre of the genre set
     * @param genre2 second genre of the genre set
     */
    public void setGenre2(String genre2){
        this.genre2 = genre2;
    }

    /**
     * gets the third genre of the genre set
     * @return the third genre of the genre set
     */
    public String getGenre3(){
        return genre3;
    }

    /**
     * sets the third genre of the genre set
     * @param genre3 third genre of the genre set
     */
    public void setGenre3(String genre3){
        this.genre3 = genre3;
    }

}
