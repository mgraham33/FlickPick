package onetoone.Friends;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class FriendsControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getAllFriends() {
        Response response = RestAssured.given().when().get("/friends");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(126, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void userExists() {
    }

    @Test
    public void getFriendListById() {
        Response response = RestAssured.given().when().get("/friends/list/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("124", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFriendListByIdNull() {
        Response response = RestAssured.given().when().get("/friends/list/1111");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("{\"message\":\"User does not exist\"}", returnString);
    }

    @Test
    public void getPfpByIDFriendsUser1() {
        Response response = RestAssured.given().when().get("/friends/119/pfp/user1");
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
    public void getPfpByIDFriendsUser1Null() {
        Response response = RestAssured.given().when().get("/friends/1111/pfp/user1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getPfpByIDFriendsUser2() {
        Response response = RestAssured.given().when().get("/friends/119/pfp/user2");
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
    public void getPfpByIDFriendsUser2Null() {
        Response response = RestAssured.given().when().get("/friends/1111/pfp/user2");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getFriendsById() {
        Response response = RestAssured.given().when().get("/friends/119");
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
    public void getFriendsByIdNull() {
        Response response = RestAssured.given().when().get("/friends/11111");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getFriendsByFriendType() {
        Response response = RestAssured.given().when().get("/friends/friendType/accepted");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(126, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSpecificFriendsByUsername() {
        Response response = RestAssured.given().when().get("/friends/11/username/chris123");
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
    public void getSpecificFriendsByUsername2() {
        Response response = RestAssured.given().when().get("/friends/1/username/chris123");
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
    public void getSpecificFriendsByUsername3() {
        Response response = RestAssured.given().when().get("/friends/8/username/chris123");
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
    public void createFriends() {
    }

    @Test
    public void updateFriends() {
//        Response response = RestAssured.given().contentType("application/json").body("{\"id\":119,\"user1\":11,\"user2\":14,\"friendtype\":\"accepted\"}").when().put("/friends/119");
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
    public void updateFriendsNull() {
        Response response = RestAssured.given().contentType("application/json").body("{\"id\":119,\"user1\":11,\"user2\":14,\"friendtype\":\"accepted\"}").when().put("/friends/1198769");
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
    public void updateFriendType() {
        Response response = RestAssured.given().contentType("application/json").when().put("/friends/119/UpdateFriendsType/accepted");
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
    public void updateFriendTypeNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/friends/119879/UpdateFriendsType/accepted");
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
    public void updateUser1() {
        Response response = RestAssured.given().contentType("application/json").when().put("/friends/119/updateUser1/11");
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
    public void updateUser1Null() {
        Response response = RestAssured.given().contentType("application/json").when().put("/friends/11879/updateUser1/11");
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
    public void updateUser2() {
        Response response = RestAssured.given().contentType("application/json").when().put("/friends/119/updateUser2/14");
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
    public void updateUser2Null() {
        Response response = RestAssured.given().contentType("application/json").when().put("/friends/119879/updateUser2/14");
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
    public void deleteFriendListById() {
    }

    @Test
    public void deleteFriends() {
    }
}