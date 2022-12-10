package onetoone.Genres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringRunner.class)
public class GenresControllerTest {

//    @LocalServerPort
//    int port;
//
//    @Before
//    public void setUp() {
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//    }

    @Test
    public void getAllGenres() {
        Response response = RestAssured.given().when().get("/genres");
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
    public void getUniqueGenres() {
        Response response = RestAssured.given().when().get("/genres/unique");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[{\"id\":\"1\", \"genre\":\"action\"},{\"id\":\"2\", \"genre\":\"thriller\"},{\"id\":\"3\", \"genre\":\"adventure\"},{\"id\":\"4\", \"genre\":\"sci-fi\"},{\"id\":\"5\", \"genre\":\"horror\"},{\"id\":\"6\", \"genre\":\"crime\"},{\"id\":\"7\", \"genre\":\"drama\"},{\"id\":\"8\", \"genre\":\"mystery\"},{\"id\":\"9\", \"genre\":\"comedy\"},{\"id\":\"10\", \"genre\":\"fantasy\"},{\"id\":\"11\", \"genre\":\"western\"},{\"id\":\"12\", \"genre\":\"sport\"},{\"id\":\"13\", \"genre\":\"war\"},{\"id\":\"14\", \"genre\":\"musical\"},{\"id\":\"15\", \"genre\":\"romance\"}]", returnString);
    }

    @Test
    public void getGenresById() {
        Response response = RestAssured.given().when().get("/genres/1");
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
    public void getMovieByIdNull() {
        Response response = RestAssured.given().when().get("/genres/99");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getGenresByName() {
        Response response = RestAssured.given().when().get("/genres/genre/horror");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(20, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createGenres() {
    }

    @Test
    public void updateGenres() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":30,\"genre1\":\"comedy\",\"genre2\":\"crime\",\"genre3\":\"\"}").when().put("/genres/30");
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
    public void updateGenresNull() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":30,\"genre1\":\"comedy\",\"genre2\":\"crime\",\"genre3\":\"\"}").when().put("/genres/30000");
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
    public void updateGenre1() {
        Response response = RestAssured.given().contentType("application/json").when().put("/genres/30/updateGenre1/comedy");
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
    public void updateGenre1Null() {
        Response response = RestAssured.given().contentType("application/json").when().put("/genres/30000/updateGenre1/comedy");
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
    public void updateGenre2() {
        Response response = RestAssured.given().contentType("application/json").when().put("/genres/30/updateGenre2/crime");
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
    public void updateGenre2Null() {
        Response response = RestAssured.given().contentType("application/json").when().put("/genres/3000000/updateGenre2/crime");
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
    public void updateGenre3() {
        Response response = RestAssured.given().contentType("application/json").when().put("/genres/30/updateGenre3/romance");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(28, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateGenre3Null() {
        Response response = RestAssured.given().contentType("application/json").when().put("/genres/300000/updateGenre3/romance");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(28, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteGenres() {
    }
}