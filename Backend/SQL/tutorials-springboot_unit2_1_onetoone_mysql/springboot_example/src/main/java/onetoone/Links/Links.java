package onetoone.Links;

import javax.persistence.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * Author: Chris Costa and Matt Graham
 */

@Entity
public class Links {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the Links set", name="id", required=true)
    private int id;
    @ApiModelProperty(notes = "Netflix link", name="netflix", required=true)
    private String netflix;
    @ApiModelProperty(notes = "Hulu link", name="hulu", required=true)
    private String hulu;
    @ApiModelProperty(notes = "HBO Max link", name="hbo_max", required=true)
    private String hbo_max;
    @ApiModelProperty(notes = "Disney+ link", name="disney_plus", required=true)
    private String disney_plus;
    @ApiModelProperty(notes = "Amazon Prime Video link", name="amazon_prime", required=true)
    private String amazon_prime;

    /**
     *
     * @param netflix Netflix link
     * @param hulu Hulu link
     * @param hbo_max HBOMax link
     * @param disney_plus Disney+ link
     * @param amazon_prime Amazon Prime Video Link
     */
    public Links(String netflix, String hulu, String hbo_max, String disney_plus, String amazon_prime) {
        this.netflix = netflix;
        this.hulu = hulu;
        this.hbo_max = hbo_max;
        this.disney_plus = disney_plus;
        this.amazon_prime = amazon_prime;
    }

    /**
     * default constructor
     */
    public Links() {
    }

    /**
     * gets id of link set
     * @return id of link set
     */
    public int getId(){
        return id;
    }

    /**
     * sets id of link set
     * @param id id of link set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * gets Netflix link
     * @return Netflix link
     */
    public String getNetflix(){
        return netflix;
    }

    /**
     * sets Netflix Link
     * @param netflix Netflix link
     */
    public void setNetflix(String netflix){
        this.netflix = netflix;
    }

    /**
     * gets Hulu link
     * @return Hulu link
     */
    public String getHulu(){
        return hulu;
    }

    /**
     * sets Hulu link
     * @param hulu Hulu link
     */
    public void setHulu(String hulu){
        this.hulu = hulu;
    }

    /**
     * gets HBOMax link
     * @return HBOMax link
     */
    public String getHboMax(){
        return hbo_max;
    }

    /**
     * sets HBOMax link
     * @param hbo_max HBOMax link
     */
    public void setHboMax(String hbo_max){
        this.hbo_max = hbo_max;
    }

    /**
     * gets Disney+ link
     * @return Disney+ link
     */
    public String getDisneyPlus(){
        return disney_plus;
    }

    /**
     * sets Disney+ link
     * @param disney_plus Disney+ link
     */
    public void setDisneyPlus(String disney_plus){
        this.disney_plus = disney_plus;
    }

    /**
     * gets Amazon Prime Video link
     * @return Amazon Prime Video link
     */
    public String getAmazonPrime(){
        return amazon_prime;
    }

    /**
     * sets Amazon Prime Video link
     * @param amazon_prime Amazon Prime Video link
     */
    public void setAmazonPrime(String amazon_prime){
        this.amazon_prime = amazon_prime;
    }

}
