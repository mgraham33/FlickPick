package onetoone.Genres;

import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.ApiParam;
import onetoone.Movies.Movies;
import onetoone.Movies.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Author: Chris Costa and Matt Graham
 */

@RestController
@Api(value = "Swagger2DemoRestController")
public class GenresController {

    @Autowired
    GenresRepository genresRepository;

    @Autowired
    MoviesRepository moviesRepository;

    // return message for successful action
    private String success = "{\"message\":\"success\"}";
    // return message for action where genre set does not exist
    private String dne = "{\"message\":\"Genre does not exist\"}";
    // return message for action where movie does not exist
    private String mDne = "{\"message\":\"Movie does not exist\"}";

    /**
     * gets all genre sets in the repository
     * @return all genre sets
     */
    @ApiOperation(value = "Get list of Genre sets in the database", response = Iterable.class)
    @GetMapping(path = "/genres")
    List<Genres> getAllGenres(){
        return genresRepository.findAll();
    }

    /**
     * gets list of unique genres
     * @return JSON list of unique genres
     */
    @ApiOperation(value = "Get list of unique Genres", response = Iterable.class)
    @GetMapping(path = "/genres/unique")
    String getUniqueGenres() {
        // gets all genre sets
        List<Genres> genresList = genresRepository.findAll();
        List<String> genres = new ArrayList<String>();
        for (int i = 0; i < genresList.size(); i++) {
            // gets each genre of a set
            String g1 = genresList.get(i).getGenre1().toLowerCase();
            String g2 = genresList.get(i).getGenre2().toLowerCase();
            String g3 = genresList.get(i).getGenre3().toLowerCase();
            // adds the genre if not null and not already in the list
            if (!g1.equals("") && !(genres.contains(g1))) {
                genres.add(g1);
            }
            if (!g2.equals("") && !(genres.contains(g2))) {
                genres.add(g2);
            }
            if (!g3.equals("") && !(genres.contains(g3))) {
                genres.add(g3);
            }
        }
        // converts list to JSON
        String unique = "[";
        for (int i = 0; i < genres.size(); i++) {
            unique += "{\"id\":\"" + (i + 1) + "\", \"genre\":\"" + genres.get(i) + "\"}";
            if (i + 1 < genres.size()) {
                unique += ",";
            }
        }
        unique += "]";
        // returns the list
        return unique;
    }

    /**
     * gets genre set with given id
     * @param id id of genre list
     * @return genre list with given id
     */
    @ApiOperation(value = "Get a Genre set by id", response = Genres.class)
    @GetMapping(path = "/genres/{id}")
    Genres getGenresById(@PathVariable("id") @ApiParam(name = "id", value = "Genres set id", example = "1") int id){
        // returns the genres set if not null
        if(genresRepository.findById(id) == null){
            return null;
        }
        return genresRepository.findById(id);
    }

    /**
     * gets all genre sets containing a specified genre
     * @param genre specified genre
     * @return all genre sets containing the specified genre
     */
    @ApiOperation(value = "Get list of Genre sets with a given Genre", response = Iterable.class)
    @GetMapping(path = "/genres/genre/{genre}")
    List<Genres> getGenresByName(@PathVariable("genre") @ApiParam(name = "genre", value = "Genre name", example = "action") String genre){
        // gets all genre sets
        List<Genres> genres = genresRepository.findAll();
        List<Genres> g = new ArrayList<Genres>();
        // converts genre to lowercase
        String genLow = genre.toLowerCase();
        for (int i = 0; i < genres.size(); i++) {
            Genres gen = genres.get(i);
            // checks if genre set contains the genre
            if (gen.getGenre1().equals(genLow) || gen.getGenre2().equals(genLow) || gen.getGenre3().equals(genLow)) {
                g.add(gen);
            }
        }
        // removes duplicate genre sets
        for (int i = 0; i < g.size(); i++) {
            for (int j = i + 1; j < g.size(); j++) {
                if (g.get(i) == g.get(j)) {
                    g.remove(j);
                    j--;
                }
            }
        }
        // returns the list[
        return g;
    }

    /**
     * creates a genre set with given values
     * @param genre JSON Genres object
     * @return String result of action
     */
    @ApiOperation(value = "Create a set of Genres with given values", response = String.class)
    @PostMapping(path = "/genres")
    String createGenres(@RequestBody Genres genre){
        // checks if the JSON is null
        if (genre == null) {
            return dne;
        }
        List<Genres> genres = genresRepository.findAll();
        List<Movies> movies = moviesRepository.findAll();
        // checks if the genre set is in bounds
        if (genres.size() != movies.size() - 1) {
            return mDne;
        }
        // saves the genre set
        genresRepository.save(genre);
        return success;
    }

    /**
     * updates a genre set with given values
     * @param id genre set id
     * @param request JSON Genres object
     * @return updated genre set
     */
    @ApiOperation(value = "Update a Genre set with given values", response = Genres.class)
    @PutMapping("/genres/{id}")
    Genres updateGenres(@PathVariable("id") @ApiParam(name = "id", value = "Genres set id", example = "1") int id, @RequestBody Genres request){
        Genres genre = genresRepository.findById(id);
        // checks if the genre set exists
        if(genre == null) {
            return null;
        }
        // saves the genre set and returns
        request.setId(id);
        genresRepository.save(request);
        return genresRepository.findById(id);
    }

    // individual update mappings

    /**
     * updates the first genre in a genre set
     * @param id genre set id
     * @param genre1 first genre
     * @return updated genre set
     */
    @ApiOperation(value = "Updates the first Genre in a specified Genre set", response = Genres.class)
    @PutMapping("/genres/{id}/updateGenre1/{genre1}")
    Genres updateGenre1(@PathVariable("id") @ApiParam(name = "id", value = "Genres set id", example = "1") int id, @PathVariable("genre1") @ApiParam(name = "genre1", value = "Genre 1 name", example = "action") String genre1){
        Genres g = genresRepository.findById(id);
        // checks if the genre set exists
        if(g == null) {
            return null;
        }
        // updates the genre set and returns
        g.setId(id);
        g.setGenre1(genre1);
        genresRepository.save(g);
        return genresRepository.findById(id);
    }

    /**
     * updates the second genre in a genre set
     * @param id genre set id
     * @param genre2 second genre
     * @return updated genre set
     */
    @ApiOperation(value = "Updates the second Genre in a specified Genre set", response = Genres.class)
    @PutMapping("/genres/{id}/updateGenre2/{genre2}")
    Genres updateGenre2(@PathVariable("id") @ApiParam(name = "id", value = "Genres set id", example = "1") int id, @PathVariable("genre2") @ApiParam(name = "genre2", value = "Genre 2 name", example = "action") String genre2){
        Genres g = genresRepository.findById(id);
        // checks if the genre set exists
        if(g == null) {
            return null;
        }
        // updates the genre set and returns
        g.setId(id);
        g.setGenre2(genre2);
        genresRepository.save(g);
        return genresRepository.findById(id);
    }

    /**
     * updates the third genre in a genre set
     * @param id genre set id
     * @param genre3 third genre
     * @return updated genre set
     */
    @ApiOperation(value = "Updates the third Genre in a specified Genre set", response = Genres.class)
    @PutMapping("/genres/{id}/updateGenre3/{genre3}")
    Genres updateGenre3(@PathVariable("id") @ApiParam(name = "id", value = "Genres set id", example = "1") int id, @PathVariable("genre3") @ApiParam(name = "genre3", value = "Genre 3 name", example = "action") String genre3){
        Genres g = genresRepository.findById(id);
        // checks if the genre set exists
        if(g == null) {
            return null;
        }
        // updates the genre set and returns
        g.setId(id);
        g.setGenre3(genre3);
        genresRepository.save(g);
        return genresRepository.findById(id);
    }

    /**
     * deletes a genre set with a given id
     * @param id genre set id
     * @return String result of action
     */
    @ApiOperation(value = "Deletes a Genre set", response = String.class)
    @DeleteMapping(path = "/genres/{id}")
    String deleteGenres(@PathVariable("id") @ApiParam(name = "id", value = "Genres set id", example = "1") int id){
        Genres g = genresRepository.findById(id);
        // checks if genre set exists
        if(g == null) {
            return dne;
        }
        // deletes genre set
        genresRepository.deleteById(id);
        return success;
    }

}
