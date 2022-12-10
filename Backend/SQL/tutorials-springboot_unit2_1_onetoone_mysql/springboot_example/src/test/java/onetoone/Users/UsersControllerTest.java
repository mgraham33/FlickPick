package onetoone.Users;

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

//@DataJpaTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UsersControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getAllUsers() {
        Response response = RestAssured.given().when().get("/users");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(36, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserById() {
        Response response = RestAssured.given().when().get("/users/11");
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

    public void getUserByIdnull(){
        Response response = RestAssured.given().when().get("/users/111");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void movieExists() {
    }

    @Test
    public void login() {
    }

    @Test
    public void getPfpByID() {
        //TODO
        Response response = RestAssured.given().when().get("/users/11/pfp");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(11, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPfpByIdNull(){
        Response response = RestAssured.given().when().get("/users/111/pfp");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getUserByUsername() {
        //TODO
        Response response = RestAssured.given().when().get("/users/username/chris123");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(11, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserByUsernameNull(){
        Response response = RestAssured.given().when().get("/users/username/chri23");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getUserByEmail() {
        //TODO
        Response response = RestAssured.given().when().get("/users/email/ccc@iastate.edu");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(11, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserByEmailNull(){
        Response response = RestAssured.given().when().get("/users/email/ccc@ias");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getUserByMovieID() {
        Response response = RestAssured.given().when().get("/users/favMID/3");
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
    public void getUserByMovieIDNull(){
        Response response = RestAssured.given().when().get("/users/favMID/100");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getUserByUserType() {
        Response response = RestAssured.given().when().get("/users/usertype/User");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(36, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUsersByPFP() {
        Response response = RestAssured.given().when().get("/users/pfp/%2FImages%2FPFPs%2Fcc22.jpg");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void createUser() {
    }

    @Test
    public void updateUser() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":5,\"username\":\"guest\",\"email\":\"guestUser@iastate.edu\",\"password\":\"1234567890\",\"pfp\":\"/Images/PFPs/defaultpfp.jpg\",\"favMID\":10,\"userType\":\"Guest\"}").when().put("/users/5");
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
    public void updateUserNull() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":5,\"username\":\"guest\",\"email\":\"guestUser@iastate.edu\",\"password\":\"1234567890\",\"pfp\":\"/Images/PFPs/defaultpfp.jpg\",\"favMID\":10,\"userType\":\"Guest\"}").when().put("/users/5987654");
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
    public void updateUsername() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5/updateUsername/guest");
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
    public void updateUsernameNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/987655/updateUsername/guest");
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
    public void updateEmail() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5/updateEmail/guestUser@iastate.edu");
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
    public void updateEmailNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5987/updateEmail/guestUser@iastate.edu");
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
    public void updateUserType() {
//        Response response = RestAssured.given().contentType("application/json").when().put("/users/5/updateUsertype/Guest");
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//        String returnString = response.getBody().asString();
//        try {
//            JSONArray returnArr = new JSONArray(returnString);
//            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
//            assertEquals(30, returnObj.get("id"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void updateUserTypeNull() {
//        Response response = RestAssured.given().contentType("application/json").when().put("/users/987655/updateUsertype/Guest");
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//        String returnString = response.getBody().asString();
//        try {
//            JSONArray returnArr = new JSONArray(returnString);
//            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
//            assertEquals(30, returnObj.get("id"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void updatePassword() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5/updatePassword/1234567890");
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
    public void updatePasswordNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/59876/updatePassword/1234567890");
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
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5/updateMovie/10");
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
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5987/updateMovie/10");
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
    public void updatePfp() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/5/updatePfp/%2FImages%2FPFPs%2Fdefaultpfp.jpg");
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
    public void updatePfpNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/users/598765/updatePfp/%2FImages%2FPFPs%2Fdefaultpfp.jpg");
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
    public void deleteUser() {
    }
}