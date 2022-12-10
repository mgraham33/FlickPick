package onetoone.Reviews;

import static org.junit.jupiter.api.Assertions.assertEquals;

import onetoone.Movies.MoviesRepository;
import onetoone.Users.Users;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

//@DataJpaTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ReviewsControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void userExists() {
//        ReviewsController r = new ReviewsController();
//        int exists = r.userExists(11);
//        assertEquals(1, exists);
    }

    @Test
    public void movieExists() {
//        MoviesRepository moviesRepository;
//        ReviewsController r = new ReviewsController();
//        int exists = r.movieExists(11);
//        assertEquals(1, exists);
    }

    @Test
    public void updateMovieRating() {
    }

    @Test
    public void getAllReviews() {
        Response response = RestAssured.given().when().get("/reviews");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(42, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllReviewsByMID() {
        Response response = RestAssured.given().when().get("/reviews/movie/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(42, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllReviewsByMIDNull(){
        Response response = RestAssured.given().when().get("/reviews/movie/100");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getReviewById() {
        //TODO
        Response response = RestAssured.given().when().get("/reviews/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(1, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getReviewByIdNull(){
        Response response = RestAssured.given().when().get("/reviews/1000");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getSpecificReviewsByUserId() {
        Response response = RestAssured.given().when().get("/reviews/user/11");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(23, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSpecificReviewsByUserIdNull(){
        Response response = RestAssured.given().when().get("/reviews/user/100");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getSpecificReviewsByUsername() {
        //TODO GOOOOOD
        Response response = RestAssured.given().when().get("/reviews/username/chris123");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(23, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSpecificReviewsByUsernameNull(){
        Response response = RestAssured.given().when().get("/reviews/username/chscc");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void createReview() {
    }

    @Test
    public void updateReview() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":12,\"rating\":1.0,\"movieid\":1,\"userid\":1,\"reason\":\"Bad\"}").when().put("/reviews/12");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateReviewNull() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":12,\"rating\":1.0,\"movieid\":1,\"userid\":1,\"reason\":\"Bad\"}").when().put("/reviews/123812");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateRating() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/12/updateRating/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateRatingNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/127837/updateRating/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateMovieid() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/12/updateMovieid/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateMovieidNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/129876/updateMovieid/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateUserid() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/12/updateUserid/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateUseridNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/1872/updateUserid/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateReason() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/12/updateReason/bad");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateReasonNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/reviews/12987/updateReason/bad");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(30, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteReview() {
    }
}