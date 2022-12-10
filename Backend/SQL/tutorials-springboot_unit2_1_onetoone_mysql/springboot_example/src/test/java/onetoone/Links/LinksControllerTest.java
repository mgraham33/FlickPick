package onetoone.Links;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class LinksControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getAllLinks() {
        Response response = RestAssured.given().when().get("/links");
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
    public void getLinksById() {
        Response response = RestAssured.given().when().get("/links/1");
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

    @org.junit.jupiter.api.Test
    public void getMovieByIdNull() {
        Response response = RestAssured.given().when().get("/links/99");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void createLinks() {
    }

    @Test
    public void updateLinks() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":30,\"netflix\":\"\",\"hulu\":\"https://www.hulu.com/watch/c53dd0dc-fc77-42de-b55d-157436d67bdd\",\"hboMax\":\"\",\"disneyPlus\":\"\",\"amazonPrime\":\"https://www.amazon.com/gp/video/detail/amzn1.dv.gti.b4a9f773-08fb-f692-b3e4-a9ba5ca283f5?autoplay=0&ref_=atv_cf_strg_wb\"}").when().put("/links/30");
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
    public void updateLinksNull() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":30,\"netflix\":\"\",\"hulu\":\"https://www.hulu.com/watch/c53dd0dc-fc77-42de-b55d-157436d67bdd\",\"hboMax\":\"\",\"disneyPlus\":\"\",\"amazonPrime\":\"https://www.amazon.com/gp/video/detail/amzn1.dv.gti.b4a9f773-08fb-f692-b3e4-a9ba5ca283f5?autoplay=0&ref_=atv_cf_strg_wb\"}").when().put("/links/3000000");
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
    public void updateNetflix() {
    }

    @Test
    public void updateNetflixNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/links/100000/updateNetflix/test");
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
    public void updateHulu() {
    }

    @Test
    public void updateHuluNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/links/100000/updateHulu/test");
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
    public void updateHbomax() {
    }

    @Test
    public void updateHbomaxNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/links/100000/updateHbomax/test");
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
    public void updateDisneyplus() {
    }

    @Test
    public void updateDisneyplusNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/links/100000/updateDisneyplus/test");
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
    public void updateAmazonprime() {
    }

    @Test
    public void updateAmazonprimeNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/links/100000/updateAmazonprime/test");
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
    public void deleteLinks() {
    }
}