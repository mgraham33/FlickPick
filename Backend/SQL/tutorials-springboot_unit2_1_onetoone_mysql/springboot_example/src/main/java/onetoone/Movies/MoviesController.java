package onetoone.Movies;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.swagger.annotations.*;
import onetoone.Genres.Genres;
import onetoone.Genres.GenresRepository;
import onetoone.Links.Links;
import onetoone.Links.LinksRepository;
import onetoone.Reviews.Reviews;
import onetoone.Reviews.ReviewsRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Chris Costa and Matt Graham
 */ 

@RestController
@Api(value = "Swagger2DemoRestController")
public class MoviesController {

    @Autowired
    MoviesRepository moviesRepository;

    @Autowired
    LinksRepository linksRepository;

    @Autowired
    GenresRepository genresRepository;

    @Autowired
    ReviewsRepository reviewsRepository;

    // return string for successful action
    private String success = "{\"message\":\"success\"}";
    // return string for action when movie doesn't exist
    private String dne = "{\"message\":\"Movie does not exist\"}";
    // return string for action when rating is out of bounds
    private String obRating = "{\"message\":\"Rating is out of the given range\"}";

    /**
     * gets all movies in the repository
     * @return all movies
     */
    @ApiOperation(value = "Get list of Movies in the database", response = Iterable.class)
    @GetMapping(path = "/movies")
    List<Movies> getAllMovies(){
        return moviesRepository.findAll();
    }

    /**
     * gets movie with given id
     * @param id movie id
     * @return movie with given id
     */
    @ApiOperation(value = "Get a Movie by a given id value", response = Movies.class)
    @GetMapping(path = "/movies/{id}")
    Movies getMovieById(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id){
        // checks if movie exists
        if(moviesRepository.findById(id) == null){
            return null;
        }
        // returns movie
        return moviesRepository.findById(id);
    }

    /**
     * gets thumbnail for movie with given id
     * @param id movie id
     * @return thumbnail for movie with given id
     * @throws IOException
     */
    @ApiOperation(value = "Get the thumbnail for a Movie by id", response = String.class)
    @GetMapping(path = "/movies/{id}/picture")
    String getPictureById(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id) throws IOException {
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if (m == null) {
            return null;
        }
        // converts thumbnail to Base64 encoding
        String path = m.getPicture();
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String s = Base64.encodeBase64String(bytes);
        // returns thumbnail in JSON format
        return "{\"picture\":\"" + s + "\"}";
    }

    /**
     * gets movies with given genre
     * @param genre genre
     * @return movies with given genre
     */
    @ApiOperation(value = "Get list of Movies that are listed under a specified genre", response = Iterable.class)
    @GetMapping(path = "/movies/genre/{genre}")
    List<Movies> getMoviesByGenre(@PathVariable("genre") @ApiParam(name = "genre", value = "Movie genre", example = "action") String genre) {
        // gets all movies and genres
        List<Movies> allMovies = moviesRepository.findAll();
        List<Genres> allGenres = genresRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size() && i < allGenres.size(); i++) {
            String lowGenre = genre.toLowerCase();
            // gets all genres in a genre set
            String lowG1 = allGenres.get(i).getGenre1().toLowerCase();
            String lowG2 = allGenres.get(i).getGenre2().toLowerCase();
            String lowG3 = allGenres.get(i).getGenre3().toLowerCase();
            if (allMovies.get(i).getId() == allGenres.get(i).getId() && (lowG1.equals(lowGenre) || lowG2.equals(lowGenre) || lowG3.equals(lowGenre))) {
                // adds movie to list if a genre matches
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets movies with a given release year
     * @param year release year
     * @return movies with a given release year
     */
    @ApiOperation(value = "Get list of Movies released in a given year", response = Iterable.class)
    @GetMapping(path = "/movies/year/{year}")
    List<Movies> getMoviesByYear(@PathVariable("year") @ApiParam(name = "year", value = "Release year", example = "2000") int year) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getYear() == year) {
                // adds movie to list if the year matches
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets movies with a given title
     * @param title title
     * @return movies with a given title
     */
    @ApiOperation(value = "Get list of Movies by a specified title", response = Iterable.class)
    @GetMapping(path = "/movies/title/{title}")
    List<Movies> getMoviesByTitle(@PathVariable("title") @ApiParam(name = "title", value = "Movie title", example = "Die Hard") String title) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getTitle().equals(title)) {
                // adds movie to list if title matches
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets movies on specified streaming services
     * @param netflix Netflix boolean
     * @param hulu Hulu boolean
     * @param hboMax HBOMax boolean
     * @param disneyPlus Disney+ boolean
     * @param amazonPrime Amazon Prime Video boolean
     * @return
     */
    @ApiOperation(value = "Get list of Movies available on specified streaming services", response = Iterable.class)
    @GetMapping(path = "/movies/links")
    List<Movies> getMoviesByLinks(@RequestBody String netflix, @RequestBody String hulu, @RequestBody String hboMax,
                                  @RequestBody String disneyPlus, @RequestBody String amazonPrime) {
        List<Movies> movies = new ArrayList<Movies>();
        // checks if movies have Netflix links
        if (netflix.equals(true)) {
            List<Movies> mov = getMoviesByLinksNetflix();
            for (int i = 0; i < mov.size(); i++) {
                movies.add(mov.get(i));
            }
        }
        // checks if movies have Hulu links
        if (hulu.equals(true)) {
            List<Movies> mov = getMoviesByLinksHulu();
            for (int i = 0; i < mov.size(); i++) {
                movies.add(mov.get(i));
            }
        }
        // checks if movies have HBOMax links
        if (hboMax.equals(true)) {
            List<Movies> mov = getMoviesByLinksHboMax();
            for (int i = 0; i < mov.size(); i++) {
                movies.add(mov.get(i));
            }
        }
        // checks if movies have Disney+ links
        if (disneyPlus.equals(true)) {
            List<Movies> mov = getMoviesByLinksDisneyPlus();
            for (int i = 0; i < mov.size(); i++) {
                movies.add(mov.get(i));
            }
        }
        // checks if movies have Amazon Prime Video links
        if (amazonPrime.equals(true)) {
            List<Movies> mov = getMoviesByLinksAmazonPrime();
            for (int i = 0; i < mov.size(); i++) {
                movies.add(mov.get(i));
            }
        }
        // removes duplicates from the list
        for (int i = 0; i < movies.size(); i++) {
            for (int j = i + 1; j < movies.size(); j++) {
                if (movies.get(i).getId() == movies.get(j).getId()) {
                    movies.remove(j);
                }
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets movies with a given title partial string
     * @param query title partial string
     * @return movies with a given title partial string
     */
    @ApiOperation(value = "Get list of Movies that have a title matching a partial String", response = Iterable.class)
    @GetMapping(path="/movies/search/{query}")
    List<Movies> getMoviesSearchPartialTitle(@PathVariable("query") @ApiParam(name = "query", value = "Partial String of Movie title", example = "J") String query) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> resultMovies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) {
                // adds movie to list if partial string is in the title
                resultMovies.add(allMovies.get(i));
            }
        }
        for (int i = 0; i < resultMovies.size(); i++) {
            for (int j = i + 1; j < resultMovies.size(); j++) {
                // orders titles by relevance
                if (resultMovies.get(j).getTitle().toLowerCase().indexOf(query.toLowerCase())
                        < resultMovies.get(i).getTitle().toLowerCase().indexOf(query.toLowerCase())) {
                    // swaps movies
                    Movies temp = resultMovies.get(i);
                    resultMovies.set(i, resultMovies.get(j));
                    resultMovies.set(j, temp);
                }
            }
        }
        // returns the list
        return resultMovies;
    }

    /**
     * checks for typos in movie partial title
     * @param title partial title of movie
     * @return list of movies that should match the title
     */
    @ApiOperation(value = "Checks for typos in movie title", response = String.class)
    @GetMapping(path = "/movies/typo/{title}")
    List<Movies> getMoviesSpellCheck(@PathVariable("title") @ApiParam(name = "title", value = "Partial String of Movie title", example = "Inbiana") String title) {
        // generates a dictionary from the MoviesRepository
        List<String> dictionary = new ArrayList<String>();
        List<Movies> allMovies = moviesRepository.findAll();
        title = title.toLowerCase();
        // adds full words to the dictionary
        for (int i = 0; i < allMovies.size(); i++) {
            String word = "";
            String movieTitle = allMovies.get(i).getTitle().toLowerCase();
            for (int j = 0; j < movieTitle.length(); j++) {
                if (movieTitle.charAt(j) != ' ') {
                    word += movieTitle.charAt(j);
                } else {
                    // checks for duplicates
                    if (!dictionary.contains(word)) {
                        dictionary.add(word);
                    }
                    word = "";
                }
            }
        }
        // adds characters to the dictionary
        for (int i = 0; i < allMovies.size(); i++) {
            String movieTitle = allMovies.get(i).getTitle().toLowerCase();
            for (int j = 0; j < movieTitle.length(); j++) {
                char c = movieTitle.charAt(j);
                // checks for duplicates and symbols
                if ((!dictionary.contains(c + "")) && Character.isLetterOrDigit(c)) {
                    dictionary.add(c + "");
                }
            }
        }
        // adds typos to the dictionary
        for (int i = 0; i < dictionary.size(); i++) {
            String word = "";
            for (int j = 0; j < dictionary.get(i).length(); j++) {
                // checks for missing symbols
                if (Character.isLetterOrDigit(dictionary.get(i).charAt(j))) {
                    word += dictionary.get(i).charAt(j);
                }
            }
            // checks for duplicates
            if (!dictionary.contains(word)) {
                dictionary.add(word);
            }
        }
        // stores the corrected title, defaults to original value
        String newTitle = title;
        // find similar Strings with the same length
        List<String> matchingLength = new ArrayList<String>();
        for (int i = 0; i < dictionary.size(); i++) {
            if (dictionary.get(i).length() == title.length()) {
                matchingLength.add(dictionary.get(i));
            }
        }
        // compare Strings of matching length, defaults to original value
        String bestMatchSameLength = title;
        int bestMatchSameLengthNum = 0;
        for (int i = 0; i < matchingLength.size(); i++) {
            int sameChars = 0;
            for (int j = 0; j < title.length(); j++) {
                // checks if one character is correct
                if (title.charAt(j) == matchingLength.get(i).charAt(j)) {
                    sameChars++;
                }
            }
            // checks if a better match is found
            if (sameChars > bestMatchSameLengthNum) {
                bestMatchSameLengthNum = sameChars;
                bestMatchSameLength = matchingLength.get(i);
            }
        }
        // finds Strings with one lesser length
        List<String> oneLesserLength = new ArrayList<String>();
        for (int i = 0; i < dictionary.size(); i++) {
            if (dictionary.get(i).length() == title.length() - 1) {
                oneLesserLength.add(dictionary.get(i));
            }
        }
        // compares Strings with one lesser length on the front end
        String bestMatchOneLesserLengthFront = title;
        int bestMatchOneLesserLengthNumFront = 0;
        // formats shortened title without the first character
        String oneLesserTitleFront = "";
        for (int i = 1; i < title.length(); i++) {
            oneLesserTitleFront += title.charAt(i);
        }
        for (int i = 0; i < oneLesserLength.size(); i++) {
            int sameChars = 0;
            for (int j = 0; j < oneLesserTitleFront.length(); j++) {
                // checks if one character is correct
                if (oneLesserTitleFront.charAt(j) == oneLesserLength.get(i).charAt(j)) {
                    sameChars++;
                }
            }
            // checks if a better match is found
            if (sameChars > bestMatchOneLesserLengthNumFront) {
                bestMatchOneLesserLengthNumFront = sameChars;
                bestMatchOneLesserLengthFront = oneLesserLength.get(i);
            }
        }
        // compares Strings with one lesser length on the back end
        String bestMatchOneLesserLengthEnd = title;
        int bestMatchOneLesserLengthNumEnd = 0;
        // formats shortened title without the first character
        String oneLesserTitleEnd = "";
        for (int i = 0; i < title.length() - 1; i++) {
            oneLesserTitleEnd += title.charAt(i);
        }
        for (int i = 0; i < oneLesserLength.size(); i++) {
            int sameChars = 0;
            for (int j = 0; j < oneLesserTitleEnd.length(); j++) {
                // checks if one character is correct
                if (oneLesserTitleEnd.charAt(j) == oneLesserLength.get(i).charAt(j)) {
                    sameChars++;
                }
            }
            // checks if a better match is found
            if (sameChars > bestMatchOneLesserLengthNumEnd) {
                bestMatchOneLesserLengthNumEnd = sameChars;
                bestMatchOneLesserLengthEnd = oneLesserLength.get(i);
            }
        }
        // finds Strings with one greater length
        List<String> oneGreaterLength = new ArrayList<String>();
        for (int i = 0; i < dictionary.size(); i++) {
            if (dictionary.get(i).length() == title.length() + 1) {
                oneLesserLength.add(dictionary.get(i));
            }
        }
        // compares Strings with one greater length on the front end
        String bestMatchOneGreaterLengthFront = title;
        int bestMatchOneGreaterLengthNumFront = 0;
        // formats shortened title with an arbitrary first character
        String oneGreaterTitleFront = "*";
        for (int i = 0; i < title.length(); i++) {
            oneGreaterTitleFront += title.charAt(i);
        }
        for (int i = 0; i < oneGreaterLength.size(); i++) {
            int sameChars = 0;
            for (int j = 0; j < oneGreaterTitleFront.length(); j++) {
                // checks if one character is correct
                if (oneGreaterTitleFront.charAt(j) == oneGreaterLength.get(i).charAt(j)) {
                    sameChars++;
                }
            }
            // checks if a better match is found
            if (sameChars > bestMatchOneGreaterLengthNumFront) {
                bestMatchOneGreaterLengthNumFront = sameChars;
                bestMatchOneGreaterLengthFront = oneGreaterLength.get(i);
            }
        }
        // compares Strings with one greater length on the back end
        String bestMatchOneGreaterLengthEnd = title;
        int bestMatchOneGreaterLengthNumEnd = 0;
        // formats shortened title with an arbitrary last character
        String oneGreaterTitleEnd = "";
        for (int i = 0; i < title.length(); i++) {
            oneGreaterTitleFront += title.charAt(i);
        }
        oneGreaterTitleEnd += "*";
        for (int i = 0; i < oneGreaterLength.size(); i++) {
            int sameChars = 0;
            for (int j = 0; j < oneGreaterTitleEnd.length(); j++) {
                // checks if one character is correct
                if (oneGreaterTitleEnd.charAt(j) == oneGreaterLength.get(i).charAt(j)) {
                    sameChars++;
                }
            }
            // checks if a better match is found
            if (sameChars > bestMatchOneGreaterLengthNumEnd) {
                bestMatchOneGreaterLengthNumEnd = sameChars;
                bestMatchOneGreaterLengthEnd = oneGreaterLength.get(i);
            }
        }
        // indexes the best match Strings
        ArrayList<String> bestMatchOrder = new ArrayList<String>();
        bestMatchOrder.add(bestMatchSameLength);
        bestMatchOrder.add(bestMatchOneGreaterLengthFront);
        bestMatchOrder.add(bestMatchOneGreaterLengthEnd);
        bestMatchOrder.add(bestMatchOneLesserLengthFront);
        bestMatchOrder.add(bestMatchOneLesserLengthEnd);
        // indexes the best match String character differences
        ArrayList<Integer> bestMatchNumOrder = new ArrayList<Integer>();
        bestMatchNumOrder.add(bestMatchSameLengthNum);
        bestMatchNumOrder.add(bestMatchOneGreaterLengthNumFront);
        bestMatchNumOrder.add(bestMatchOneGreaterLengthNumEnd);
        bestMatchNumOrder.add(bestMatchOneLesserLengthNumFront);
        bestMatchNumOrder.add(bestMatchOneLesserLengthNumEnd);
        // selects which best match method to use for the spell check
        int index = bestMatchNumOrder.indexOf(Collections.max(bestMatchNumOrder));
        newTitle = bestMatchOrder.get(index);
        // get movies with matching new title
        List<Movies> movies = getMoviesSearchPartialTitle(newTitle);
        // returns the list of movies
        return movies;
    }

    /**
     *
     * @param title title
     * @param lowRating low end of rating range
     * @param highRating high end of rating range
     * @param lowYear low end of year range
     * @param highYear high end of year range
     * @param lowMinutes low end of duration range
     * @param highMinutes high end of duration range
     * @param netflix movie must be on Netflix
     * @param hulu movie must be on Hulu
     * @param hboMax movie must be on HBOMax
     * @param disneyPlus movie must be on Disney+
     * @param amazonPrime movie must be on Amazon Prime Video
     * @param action action genre
     * @param adventure adventure genre
     * @param animation animation genre
     * @param biography biography genre
     * @param comedy comedy genre
     * @param crime crime genre
     * @param documentary documentary genre
     * @param drama drama genre
     * @param family family genre
     * @param fantasy fantasy genre
     * @param filmnoir film noir genre
     * @param history history genre
     * @param horror horror genre
     * @param music music genre
     * @param musical musical genre
     * @param mystery mystery genre
     * @param romance romance genre
     * @param scifi sci-fi genre
     * @param shorts shorts genre
     * @param sport sport genre
     * @param superhero superhero genre
     * @param thriller thriller genre
     * @param war war genre
     * @param western western genre
     * @return movies that match all the filters
     * @throws IOException
     */
    @ApiOperation(value = "Get list of Movies and their Genres according to specified filters", response = String.class)
    @GetMapping(path = "/movies/filters")
    String getMoviesByAllFilters(@RequestParam(defaultValue = "", required = false) String title,
                                       @RequestParam(defaultValue = "", required = false) String lowRating,
                                       @RequestParam(defaultValue = "", required = false) String highRating,
                                       @RequestParam(defaultValue = "", required = false) String lowYear,
                                       @RequestParam(defaultValue = "", required = false) String highYear,
                                       @RequestParam(defaultValue = "", required = false) String lowMinutes,
                                       @RequestParam(defaultValue = "", required = false) String highMinutes,
                                       @RequestParam(defaultValue = "", required = false) String netflix,
                                       @RequestParam(defaultValue = "", required = false) String hulu,
                                       @RequestParam(defaultValue = "", required = false) String hboMax,
                                       @RequestParam(defaultValue = "", required = false) String disneyPlus,
                                       @RequestParam(defaultValue = "", required = false) String amazonPrime,
                                       @RequestParam(defaultValue = "", required = false) String action,
                                       @RequestParam(defaultValue = "", required = false) String adventure,
                                       @RequestParam(defaultValue = "", required = false) String animation,
                                       @RequestParam(defaultValue = "", required = false) String biography,
                                       @RequestParam(defaultValue = "", required = false) String comedy,
                                       @RequestParam(defaultValue = "", required = false) String crime,
                                       @RequestParam(defaultValue = "", required = false) String documentary,
                                       @RequestParam(defaultValue = "", required = false) String drama,
                                       @RequestParam(defaultValue = "", required = false) String family,
                                       @RequestParam(defaultValue = "", required = false) String fantasy,
                                       @RequestParam(defaultValue = "", required = false) String filmnoir,
                                       @RequestParam(defaultValue = "", required = false) String history,
                                       @RequestParam(defaultValue = "", required = false) String horror,
                                       @RequestParam(defaultValue = "", required = false) String music,
                                       @RequestParam(defaultValue = "", required = false) String musical,
                                       @RequestParam(defaultValue = "", required = false) String mystery,
                                       @RequestParam(defaultValue = "", required = false) String romance,
                                       @RequestParam(defaultValue = "", required = false) String scifi,
                                       @RequestParam(defaultValue = "", required = false) String shorts,
                                       @RequestParam(defaultValue = "", required = false) String sport,
                                       @RequestParam(defaultValue = "", required = false) String superhero,
                                       @RequestParam(defaultValue = "", required = false) String thriller,
                                       @RequestParam(defaultValue = "", required = false) String war,
                                       @RequestParam(defaultValue = "", required = false) String western) throws IOException {
        List<Movies> movies = new ArrayList<Movies>();
        // counts the number of filters
        int numParams = 0;
        // checks if the partial title matches
        if (!title.equals("")) {
            numParams++;
            List<Movies> m = getMoviesSearchPartialTitle(title);
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
            // typo checking if no movies returned
            if (movies.size() == 0) {
                // gets each word in the title
                ArrayList<String> titleList = new ArrayList<String>();
                String temp = "";
                for (int i = 0; i < title.length(); i++) {
                    if (title.charAt(i) != ' ') {
                        temp += title.charAt(i);
                    } else {
                        titleList.add(temp);
                        temp = "";
                    }
                }
                // gets movies by typo String
                List<Movies> m2 = getMoviesSpellCheck(title);
                for (int i = 0; i < titleList.size(); i++) {
                    List<Movies> m3 = getMoviesSpellCheck(titleList.get(i));
                    for (int j = 0; j < m3.size(); j++) {
                        m2.add(m3.get(j));
                    }
                }
                // removes duplicates
                for (int i = 0; i < m2.size(); i++) {
                    for (int j = i + 1; j < m2.size(); j++) {
                        if (m2.get(i) == m2.get(j)) {
                            m2.remove(j);
                            j--;
                        }
                    }
                }
                for (int i = 0; i < m2.size(); i++) {
                    movies.add(m2.get(i));
                }
            }
        }
        // checks the rating range
        if (!lowRating.equals("") && !highRating.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByRatingRange(Double.parseDouble(lowRating),
                    Double.parseDouble(highRating));
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks the year range
        if (!lowYear.equals("") && !highYear.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByYearRange(Integer.parseInt(lowYear),
                    Integer.parseInt(highYear));
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks the duration range
        if (!lowMinutes.equals("") && !highMinutes.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByDurationRange(Integer.parseInt(lowMinutes),
                    Integer.parseInt(highMinutes));
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for Netflix links
        if (!netflix.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByLinksNetflix();
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for Hulu links
        if (!hulu.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByLinksHulu();
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks fore HBOMax links
        if (!hboMax.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByLinksHboMax();
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for Disney+ links
        if (!disneyPlus.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByLinksDisneyPlus();
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for Amazon Prime Video links
        if (!amazonPrime.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByLinksAmazonPrime();
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for action genre
        if (!action.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("action");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for adventure genre
        if (!adventure.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("adventure");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for animation genre
        if (!animation.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("animation");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for biography genre
        if (!biography.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("biography");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for comedy genre
        if (!comedy.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("comedy");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for crime genre
        if (!crime.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("crime");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for documentary genre
        if (!documentary.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("documentary");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for drama genre
        if (!drama.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("drama");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for family genre
        if (!family.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("family");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for fantasy genre
        if (!fantasy.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("fantasy");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for film noir genre
        if (!filmnoir.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("film noir");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for history genre
        if (!history.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("history");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for horror genre
        if (!horror.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("horror");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for music genre
        if (!music.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("music");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for musical genre
        if (!musical.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("musical");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for mystery genre
        if (!mystery.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("mystery");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for romance genre
        if (!romance.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("romance");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for sci-fi genre
        if (!scifi.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("sci-fi");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for shorts genre
        if (!shorts.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("short");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for sports genre
        if (!sport.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("sport");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for superhero genre
        if (!superhero.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("superhero");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for thriller genre
        if (!thriller.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("thriller");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for war genre
        if (!war.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("war");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        // checks for western genre
        if (!western.equals("")) {
            numParams++;
            List<Movies> m = getMoviesByGenre("western");
            for (int i = 0; i < m.size(); i++) {
                movies.add(m.get(i));
            }
        }
        List<Movies> moviesFinal = new ArrayList<Movies>();
        for (int i = 0; i < movies.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < movies.size(); j++) {
                // checks how many times a movie is listed
                if (movies.get(i).getId() == movies.get(j).getId()) {
                    count++;
                }
            }
            // checks if a movie meets all filters
            if (count == numParams) {
                moviesFinal.add(movies.get(i));
            } else {
                for (int j = i + 1; j < movies.size(); j++) {
                    // removes duplicates
                    if (movies.get(i).getId() == movies.get(j).getId()) {
                        movies.remove(j);
                        j--;
                    }
                }
                movies.remove(i);
            }
        }
        String moviesAndGenres = "[";
        // formats movies and genres to JSON
        for (int i = 0; i < moviesFinal.size(); i++) {
            moviesAndGenres += "{";
            moviesAndGenres += "\"id\":\"" + moviesFinal.get(i).getId() + "\",";
            moviesAndGenres += "\"title\":\"" + moviesFinal.get(i).getTitle() + "\",";
            moviesAndGenres += "\"minutes\":\"" + moviesFinal.get(i).getMinutes() + "\",";
            moviesAndGenres += "\"year\":\"" + moviesFinal.get(i).getYear() + "\",";
            moviesAndGenres += "\"rating\":\"" + moviesFinal.get(i).getRating() + "\",";
            moviesAndGenres += "\"description\":\"" + moviesFinal.get(i).getDescription() + "\",";
            // converts thumbnail to Base64 encoding
            String path = moviesFinal.get(i).getPicture();
            File file = new File(path);
            byte[] bytes = Files.readAllBytes(file.toPath());
            String s = Base64.encodeBase64String(bytes);
            moviesAndGenres += "\"picture\":\"" + s + "\",";
            // gets associated genres
            if (genresRepository.findById(i + 1).getGenre1() != null) {
                moviesAndGenres += "\"genre1\":\"" + genresRepository.findById(i + 1).getGenre1() + "\",";
            } else {
                moviesAndGenres += "\"genre1\":\"\",";

            }
            if (genresRepository.findById(i + 1).getGenre2() != null) {
                moviesAndGenres += "\"genre2\":\"" + genresRepository.findById(i + 1).getGenre2() + "\",";
            } else {
                moviesAndGenres += "\"genre2\":\"\",";

            }
            if (genresRepository.findById(i + 1).getGenre3() != null) {
                moviesAndGenres += "\"genre3\":\"" + genresRepository.findById(i + 1).getGenre3() + "\"";
            } else {
                moviesAndGenres += "\"genre3\":\"\"";

            }
            moviesAndGenres += "}";
            if ((i + 1) < moviesFinal.size()) {
                moviesAndGenres += ",";
            }
        }
        moviesAndGenres += "]";
        // returns list
        return moviesAndGenres;
    }

    /**
     * gets movies with Netflix links
     * @return movies with Netflix links
     */
    @ApiOperation(value = "Get list of Movies available on Netflix", response = Iterable.class)
    @GetMapping(path = "/movies/links/netflix")
    List<Movies> getMoviesByLinksNetflix() {
        // gets all links and movies
        List<Links> allLinks = linksRepository.findAll();
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allLinks.size(); i++) {
            if (allLinks.get(i).getNetflix() != null && !(allLinks.get(i).getNetflix().equals(""))) {
                // adds movie to list if it has a Netflix link
                movies.add(allMovies.get(i));
            }
        }
        // returns list
        return movies;
    }

    /**
     * gets movies with Hulu links
     * @return movies with Hulu links
     */
    @ApiOperation(value = "Get list of Movies available on Hulu", response = Iterable.class)
    @GetMapping(path = "/movies/links/hulu")
    List<Movies> getMoviesByLinksHulu() {
        // gets all links and movies
        List<Links> allLinks = linksRepository.findAll();
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allLinks.size(); i++) {
            if (allLinks.get(i).getHulu() != null && !(allLinks.get(i).getHulu().equals(""))) {
                // adds movie to list if it has a Hulu link
                movies.add(allMovies.get(i));
            }
        }
        // returns list
        return movies;
    }

    /**
     * gets movies with HBOMax links
     * @return movies with HBOMax links
     */
    @ApiOperation(value = "Get list of Movies available on HBO Max", response = Iterable.class)
    @GetMapping(path = "/movies/links/hboMax")
    List<Movies> getMoviesByLinksHboMax() {
        // gets all links and movies
        List<Links> allLinks = linksRepository.findAll();
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allLinks.size(); i++) {
            if (allLinks.get(i).getHboMax() != null && !(allLinks.get(i).getHboMax().equals(""))) {
                // adds movie to list if it has a HBOMax link
                movies.add(allMovies.get(i));
            }
        }
        // returns link
        return movies;
    }

    /**
     * gets movies with Disney+ links
     * @return movies with Disney+ links
     */
    @ApiOperation(value = "Get list of Movies available on Disney+", response = Iterable.class)
    @GetMapping(path = "/movies/links/disneyPlus")
    List<Movies> getMoviesByLinksDisneyPlus() {
        // gets all links and movies
        List<Links> allLinks = linksRepository.findAll();
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allLinks.size(); i++) {
            if (allLinks.get(i).getDisneyPlus() != null && !(allLinks.get(i).getDisneyPlus().equals(""))) {
                // adds movie if it has a Disney+ link
                movies.add(allMovies.get(i));
            }
        }
        // returns list
        return movies;
    }

    /**
     * gets movies with Amazon Prime Video links
     * @return movies with Amazon Prime Video links
     */
    @ApiOperation(value = "Get list of Movies available on Amazon Prime Video", response = Iterable.class)
    @GetMapping(path = "/movies/links/amazonPrime")
    List<Movies> getMoviesByLinksAmazonPrime() {
        // gets all links and movies
        List<Links> allLinks = linksRepository.findAll();
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allLinks.size(); i++) {
            if (allLinks.get(i).getAmazonPrime() != null && !(allLinks.get(i).getAmazonPrime().equals(""))) {
                // adds movie to list if it has an Amazon Prime Video link
                movies.add(allMovies.get(i));
            }
        }
        // returns list
        return movies;
    }

    /**
     * gets movies with release years in given range
     * @param lowYear low end of year range
     * @param highYear high end of year range
     * @return movies with release years in given range
     */
    @ApiOperation(value = "Get list of Movies released within a given range of years", response = Iterable.class)
    @GetMapping(path = "/movies/year/range/{lowYear}/{highYear}")
    List<Movies> getMoviesByYearRange(@PathVariable("lowYear") @ApiParam(name = "lowYear", value = "Lower end of release year range", example = "1980") int lowYear, @PathVariable("highYear") @ApiParam(name = "highYear", value = "High end of release year range", example = "3000") int highYear) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getYear() >= lowYear && allMovies.get(i).getYear() <= highYear) {
                // adds movie to the list if the release year is in the range
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets movies with a given rating
     * @param rating rating
     * @return movies with a given rating
     */
    @ApiOperation(value = "Get list of Movies with a specified rating", response = Iterable.class)
    @GetMapping(path = "/movies/rating/{rating}")
    List<Movies> getMoviesByRating(@PathVariable("rating") @ApiParam(name = "rating", value = "Movie rating", example = "3.5") double rating) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getRating() == rating) {
                // adds movie to list if rating matches
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    @ApiOperation(value = "Get list of Movies with a rating in a specified range", response = Iterable.class)
    @GetMapping(path = "/movies/rating/range/{lowRating}/{highRating}")
    List<Movies> getMoviesByRatingRange(@PathVariable("lowRating") @ApiParam(name = "lowRating", value = "Low end of Movie rating range", example = "1.0") double lowRating, @PathVariable("highRating") @ApiParam(name = "highRating", value = "High end of Movie rating range", example = "4.0") double highRating) {
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getRating() >= lowRating && allMovies.get(i).getRating() <= highRating) {
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets all movies with given duration
     * @param minutes duration in minutes
     * @return all movies with given duration
     */
    @ApiOperation(value = "Get list of Movies with a specified duration", response = Iterable.class)
    @GetMapping(path = "/movies/duration/{minutes}")
    List<Movies> getMoviesByDuration(@PathVariable("minutes") @ApiParam(name = "minutes", value = "Movie duration in minutes", example = "120") int minutes) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getMinutes() == minutes) {
                // adds movie to list if duration matches
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * gets movies with duration in given range
     * @param lowMinutes low end of minutes range
     * @param highMinutes high end of minutes range
     * @return movies with duration in given range
     */
    @ApiOperation(value = "Get list of Movies with a duration within a specified range", response = Iterable.class)
    @GetMapping(path = "/movies/duration/range/{lowMinutes}/{highMinutes}")
    List<Movies> getMoviesByDurationRange(@PathVariable("lowMinutes") @ApiParam(name = "lowMinutes", value = "Low end of Movie duration range", example = "100") int lowMinutes, @PathVariable("highMinutes") @ApiParam(name = "highMinutes", value = "High end of Movie duration range", example = "200") int highMinutes) {
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        List<Movies> movies = new ArrayList<Movies>();
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getMinutes() >= lowMinutes && allMovies.get(i).getMinutes() <= highMinutes) {
                // adds movie to list if duration is in range
                movies.add(allMovies.get(i));
            }
        }
        // returns the list
        return movies;
    }

    /**
     * creates a movie with given values
     * @param movies JSON Movies object
     * @return String result of action
     */
    @ApiOperation(value = "Creates a Movie with given values", response = String.class)
    @PostMapping(path = "/movies")
    String createMovie(@RequestBody Movies movies){
        // gets all movies
        List<Movies> allMovies = moviesRepository.findAll();
        String duplicate = "{\"message\":\"Movie is already in the DataBase\"}";
        // checks if movie exists
        if (movies == null) {
            return dne;
        }
        // check if the rating is in range
        if (movies.getRating() > 5 || movies.getRating() < 0) {
            return obRating;
        }
        // checks if the movie is already in the database with a different id
        for(int i = 0; i < allMovies.size(); i++){
            if(movies.getTitle().equals(allMovies.get(i).getTitle()) && movies.getMinutes() == allMovies.get(i).getMinutes()
                    && movies.getYear() == allMovies.get(i).getYear()){
                return duplicate;
            }
        }
        // saves the movie and returns
        moviesRepository.save(movies);
        return success;
    }

    /**
     * updates movie with given values
     * @param id movie id
     * @param request Movies JSON object
     * @return updated movie
     */
    @ApiOperation(value = "Updates a Movie with given values", response = Movies.class)
    @PutMapping(path = "/movies/{id}")
    Movies updateMovie(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @RequestBody Movies request){
        Movies movies = moviesRepository.findById(id);
        // checks if movie exists
        if(movies == null) {
            return null;
        }
        // check if the rating is in range
        if (request.getRating() > 5 || request.getRating() < 0) {
            return null;
        }
        // saves movie and returns
        request.setId(id);
        moviesRepository.save(request);
        return moviesRepository.findById(id);
    }

    // individual update mappings

    /**
     * updates title of given movie
     * @param id movie id
     * @param title title
     * @return updated movie
     */
    @ApiOperation(value = "Update the title of a Movie", response = Movies.class)
    @PutMapping(path = "/movies/{id}/updateTitle/{title}")
    Movies updateTitle(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @PathVariable("title") @ApiParam(name = "title", value = "Movie title", example = "Die Hard") String title){
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if(m == null) {
            return null;
        }
        // updates movie and returns
        m.setId(id);
        m.setTitle(title);
        moviesRepository.save(m);
        return moviesRepository.findById(id);
    }

    /**
     * updates duration of given movie
     * @param id movie id
     * @param minutes duration in minutes
     * @return updated movie
     */
    @ApiOperation(value = "Update the duration of a Movie", response = Movies.class)
    @PutMapping(path = "/movies/{id}/updateMinutes/{minutes}")
    Movies updateMinutes(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @PathVariable("minutes") @ApiParam(name = "minutes", value = "Movie duration in minutes", example = "100") int minutes){
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if(m == null) {
            return null;
        }
        // updates movie and returns
        m.setId(id);
        m.setMinutes(minutes);
        moviesRepository.save(m);
        return moviesRepository.findById(id);
    }

    /**
     * updates year of given movie
     * @param id movie id
     * @param year release year
     * @return updated movie
     */
    @ApiOperation(value = "Update the release year of a Movie", response = Movies.class)
    @PutMapping(path = "/movies/{id}/updateYear/{year}")
    Movies updateYear(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @PathVariable("year") @ApiParam(name = "year", value = "Movie release year", example = "1999") int year){
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if(m == null) {
            return null;
        }
        // updates movie and returns
        m.setId(id);
        m.setYear(year);
        moviesRepository.save(m);
        return moviesRepository.findById(id);
    }

    /**
     * updates rating of given movie
     * @param id movie id
     * @param rating average rating
     * @return updated movie
     */
    @ApiOperation(value = "Update the rating of a Movie", response = Movies.class)
    @PutMapping(path = "/movies/{id}/updateRating/{rating}")
    Movies updateRating(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @PathVariable("rating") @ApiParam(name = "rating", value = "Movie rating", example = "4.5") double rating){
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if(m == null) {
            return null;
        }
        // check if the rating is in range
        if (rating > 5 || rating < 0) {
            return null;
        }
        // updates movie and returns
        m.setId(id);
        m.setRating(rating);
        moviesRepository.save(m);
        return moviesRepository.findById(id);
    }

    /**
     * updates description of given movie
     * @param id movie id
     * @param description description
     * @return updated movie
     */
    @ApiOperation(value = "Update the description of a Movie", response = Movies.class)
    @PutMapping(path = "/movies/{id}/updateDescription/{description}")
    Movies updateDescription(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @PathVariable("description") @ApiParam(name = "description", value = "Movie description", example = "Awesome movie about stuff") String description){
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if(m == null) {
            return null;
        }
        // updates movie and returns
        m.setId(id);
        m.setDescription(description);
        moviesRepository.save(m);
        return moviesRepository.findById(id);
    }

    /**
     * updates thumbnail of given movie
     * @param id movie id
     * @param picture thumbnail
     * @return updated movie
     */
    @ApiOperation(value = "Update the thumbnail of a Movie", response = Movies.class)
    @PutMapping(path = "/movies/{id}/updatePicture/{picture}")
    Movies updatePicture(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id, @PathVariable("picture") @ApiParam(name = "picture", value = "Movie thumbnail", example = "dieHard.jpg") String picture){
        Movies m = moviesRepository.findById(id);
        // checks if movie exists
        if(m == null) {
            return null;
        }
        // updates movie and returns
        m.setId(id);
        m.setPicture("/Images/Thumbnails/" + picture);
        moviesRepository.save(m);
        return moviesRepository.findById(id);
    }

    /**
     * deletes movie with given id
     * @param id movie id
     * @return String result of action
     */
    @ApiOperation(value = "Deletes a Movie and all associated data", response = String.class)
    @DeleteMapping(path = "/movies/{id}")
    String deleteMovie(@PathVariable("id") @ApiParam(name = "id", value = "Movie id", example = "1") int id) {
        // checks if movie, associated links, and associated genres exist
        Movies m = moviesRepository.findById(id);
        Links l = linksRepository.findById(id);
        Genres g = genresRepository.findById(id);
        if(m == null) {
            return dne;
        }
        // deletes associated values
        if(l != null){
            linksRepository.deleteById(id);
        }
        if(g != null){
            genresRepository.deleteById(id);
        }
        // deletes associated reviews
        List<Reviews> reviews = reviewsRepository.findAll();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getMovieid() == id) {
                reviewsRepository.deleteById(id);
            }
        }
        // deletes movie and returns
        moviesRepository.deleteById(id);
        return success;
    }
}
