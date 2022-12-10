package onetoone.Movies;

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
public class MoviesControllerTest {

//    @LocalServerPort
//    int port;
//
//    @Before
//    public void setUp() {
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//    }

    @Test
    public void getAllMovies() {
        Response response = RestAssured.given().when().get("/movies");
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
    public void getMovieById() {
        Response response = RestAssured.given().when().get("/movies/1");
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
        Response response = RestAssured.given().when().get("/movies/99");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getPictureById() {
        Response response = RestAssured.given().when().get("/movies/1/picture");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("{\"picture\":\"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\"}", returnString);
    }

    @Test
    public void getPictureByIdNull() {
        Response response = RestAssured.given().when().get("/movies/99/picture");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("", returnString);
    }

    @Test
    public void getMoviesByGenre() {
        Response response = RestAssured.given().when().get("/movies/genre/action");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(10, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByYear() {
        Response response = RestAssured.given().when().get("/movies/year/1987");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(9, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByTitle() {
        Response response = RestAssured.given().when().get("/movies/title/Jaws");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(17, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByLinks() {
    }

    @Test
    public void getMoviesSearchPartialTitle() {
        Response response = RestAssured.given().when().get("/movies/search/d");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(16, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck() {
        Response response = RestAssured.given().when().get("/movies/typo/inbiana");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(4, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck2() {
        Response response = RestAssured.given().when().get("/movies/typo/indian");
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
    public void getMoviesSpellCheck3() {
        Response response = RestAssured.given().when().get("/movies/typo/indianaa");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(4, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck4() {
        Response response = RestAssured.given().when().get("/movies/typo/iindiana");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(4, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck5() {
        Response response = RestAssured.given().when().get("/movies/typo/ndiana");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(10, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck6() {
        Response response = RestAssured.given().when().get("/movies/typo/a");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(21, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck7() {
        Response response = RestAssured.given().when().get("/movies/typo/strangeloves");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(25, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesSpellCheck8() {
        Response response = RestAssured.given().when().get("/movies/typo/jaw");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(18, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFilters() {
        Response response = RestAssured.given().when().get("/movies/filters");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersTitle() {
        Response response = RestAssured.given().when().get("/movies/filters?title=Jaws");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("17", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersTitleTypo() {
        Response response = RestAssured.given().when().get("/movies/filters?title=Inbiana");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("4", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersTitleTypo2() {
        Response response = RestAssured.given().when().get("/movies/filters?title=Indian");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("4", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersTitleTypo3() {
        Response response = RestAssured.given().when().get("/movies/filters?title=Indianaa");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("4", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersTitleTypo4() {
        Response response = RestAssured.given().when().get("/movies/filters?title=IIndiana");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("4", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersTitleTypo5() {
        Response response = RestAssured.given().when().get("/movies/filters?title=ndiana");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("4", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersRating() {
        Response response = RestAssured.given().when().get("/movies/filters?lowRating=0&highRating=3");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("30", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersYear() {
        Response response = RestAssured.given().when().get("/movies/filters?lowYear=1990&highYear=2020");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("20", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersMinutes() {
        Response response = RestAssured.given().when().get("/movies/filters?lowMinutes=90&highMinutes=140");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("30", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersNetflix() {
        Response response = RestAssured.given().when().get("/movies/filters?netflix=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("21", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersHulu() {
        Response response = RestAssured.given().when().get("/movies/filters?hulu=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("30", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersHBO() {
        Response response = RestAssured.given().when().get("/movies/filters?hboMax=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("28", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersDisney() {
        Response response = RestAssured.given().when().get("/movies/filters?disneyPlus=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersAmazon() {
        Response response = RestAssured.given().when().get("/movies/filters?amazonPrime=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("30", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersAction() {
        Response response = RestAssured.given().when().get("/movies/filters?action=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("10", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersAdventure() {
        Response response = RestAssured.given().when().get("/movies/filters?adventure=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("21", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersAnimation() {
        Response response = RestAssured.given().when().get("/movies/filters?animation=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersBiography() {
        Response response = RestAssured.given().when().get("/movies/filters?biography=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersComedy() {
        Response response = RestAssured.given().when().get("/movies/filters?comedy=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("30", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersCrime() {
        Response response = RestAssured.given().when().get("/movies/filters?crime=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("30", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersDocumentary() {
        Response response = RestAssured.given().when().get("/movies/filters?documentary=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersDrama() {
        Response response = RestAssured.given().when().get("/movies/filters?drama=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("28", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersFamily() {
        Response response = RestAssured.given().when().get("/movies/filters?family=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersFantasy() {
        Response response = RestAssured.given().when().get("/movies/filters?fantasy=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("21", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersFilmNoir() {
        Response response = RestAssured.given().when().get("/movies/filters?filmnoir=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersHistory() {
        Response response = RestAssured.given().when().get("/movies/filters?history=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersHorror() {
        Response response = RestAssured.given().when().get("/movies/filters?horror=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("20", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersMusic() {
        Response response = RestAssured.given().when().get("/movies/filters?music=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersMusical() {
        Response response = RestAssured.given().when().get("/movies/filters?musical=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("26", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersMystery() {
        Response response = RestAssured.given().when().get("/movies/filters?mystery=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("20", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersRomance() {
        Response response = RestAssured.given().when().get("/movies/filters?romance=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("28", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersSciFi() {
        Response response = RestAssured.given().when().get("/movies/filters?scifi=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("13", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersShorts() {
        Response response = RestAssured.given().when().get("/movies/filters?shorts=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersSport() {
        Response response = RestAssured.given().when().get("/movies/filters?sport=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("24", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersSuperhero() {
        Response response = RestAssured.given().when().get("/movies/filters?superhero=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByAllFiltersThriller() {
        Response response = RestAssured.given().when().get("/movies/filters?thriller=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("17", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersWar() {
        Response response = RestAssured.given().when().get("/movies/filters?war=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("25", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByAllFiltersWestern() {
        Response response = RestAssured.given().when().get("/movies/filters?western=yes");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("23", returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByLinksNetflix() {
        Response response = RestAssured.given().when().get("/movies/links/netflix");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(21, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByLinksHulu() {
        Response response = RestAssured.given().when().get("/movies/links/hulu");
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
    public void getMoviesByLinksHboMax() {
        Response response = RestAssured.given().when().get("/movies/links/hboMax");
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
    public void getMoviesByLinksDisneyPlus() {
        Response response = RestAssured.given().when().get("/movies/links/disneyPlus");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        assertEquals("[]", returnString);
    }

    @Test
    public void getMoviesByLinksAmazonPrime() {
        Response response = RestAssured.given().when().get("/movies/links/amazonPrime");
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
    public void getMoviesByYearRange() {
        Response response = RestAssured.given().when().get("/movies/year/range/1980/2000");
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
    public void getMoviesByRating() {
        Response response = RestAssured.given().when().get("/movies/rating/2.5");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(22, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByRatingRange() {
        Response response = RestAssured.given().when().get("/movies/rating/range/1/4");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(22, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByDuration() {
        Response response = RestAssured.given().when().get("/movies/duration/102");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals(9, returnObj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMoviesByDurationRange() {
        Response response = RestAssured.given().when().get("/movies/duration/range/100/150");
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
    public void createMovie() {
//        Movies m = new Movies("test", 100, 2000, 0.0, "desc", "img");
//        Response response = RestAssured.given().contentType("application/json").body(m).when().post("/movies");
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//        String returnString = response.getBody().asString();
//        assertEquals("{\"message\":\"success\"}", returnString);
    }

    @Test
    public void updateMovie() {
        Response response = RestAssured.given().contentType("application/json").body("{" +
                "    \"id\": 30," +
                "    \"title\": \"Stir Crazy\"," +
                "    \"minutes\": 111," +
                "    \"year\": 1980," +
                "    \"rating\": 0.0," +
                "    \"description\": \"Set up and wrongfully accused, two best friends will be sent to prison for a crime they did not commit. However, no prison cell could keep them locked in.\"," +
                "    \"picture\": \"/Images/Thumbnails/stirCrazy.jpg\"" +
                "}").when().put("/movies/30");
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
    public void updateMovie2() {
        Response response = RestAssured.given().contentType("application/json").body("{" +
                "    \"id\": 30," +
                "    \"title\": \"Stir Crazy\"," +
                "    \"minutes\": 111," +
                "    \"year\": 1980," +
                "    \"rating\": 10," +
                "    \"description\": \"Set up and wrongfully accused, two best friends will be sent to prison for a crime they did not commit. However, no prison cell could keep them locked in.\"," +
                "    \"picture\": \"/Images/Thumbnails/stirCrazy.jpg\"" +
                "}").when().put("/movies/30");
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
    public void updateMovieNull() {
        Response response = RestAssured.given().contentType("application/json").body("{" +
                "    \"id\": 30," +
                "    \"title\": \"Stir Crazy\"," +
                "    \"minutes\": 111," +
                "    \"year\": 1980," +
                "    \"rating\": 0.0," +
                "    \"description\": \"Set up and wrongfully accused, two best friends will be sent to prison for a crime they did not commit. However, no prison cell could keep them locked in.\"," +
                "    \"picture\": \"/Images/Thumbnails/stirCrazy.jpg\"" +
                "}").when().put("/movies/555");
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
    public void updateTitle() {
//        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30/updateTitle/Stir%20Crazy");
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
    public void updateTitleNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30000/updateTitle/Stir%20Crazy");
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
    public void updateMinutes() {
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30/updateMinutes/111");
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
    public void updateMinutesNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/300000/updateMinutes/111");
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
    public void updateYear() {
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30/updateYear/1980");
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
    public void updateYearNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30000/updateYear/1980");
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
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30/updateRating/0");
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
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/300000/updateRating/0");
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
    public void updateDescription() {
    }

    @Test
    public void updateDescriptionNull() {
        Response response = RestAssured.given().contentType("application/json").when().put("/movies/300000000/updateDescription/test");
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
    public void updatePicture() {
//        Response response = RestAssured.given().contentType("application/json").when().put("/movies/30/updatePicture/%2FImages%2FThumbnails%2FstirCrazy.jpg");
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
    public void updatePictureNull() {
//        Response response = RestAssured.given().contentType("application/json").when().put("/movies/3000000/updatePicture/%2FImages%2FThumbnails%2FstirCrazy.jpg");
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
    public void deleteMovie() {
    }
}