package onetoone.Links;

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
public class LinksController {

    @Autowired
    LinksRepository linksRepository;

    @Autowired
    MoviesRepository moviesRepository;

    // return String for successful action
    private String success = "{\"message\":\"success\"}";
    // return String for action where link set does not exist
    private String dne = "{\"message\":\"Link does not exist\"}";
    // return String for action where movie does not exist
    private String mDne = "{\"message\":\"Movie does not exist\"}";

    /**
     * gets all link sets in the repository
     * @return all link sets
     */
    @ApiOperation(value = "Get list of Links sets in the database", response = Iterable.class)
    @GetMapping(path = "/links")
    List<Links> getAllLinks(){
        return linksRepository.findAll();
    }

    /**
     * get link set with given id
     * @param id link set id
     * @return link set with given id
     */
    @ApiOperation(value = "Get a Links set by id", response = Links.class)
    @GetMapping(path = "/links/{id}")
    Links getLinksById(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id){
        // checks if link set exists
        if(linksRepository.findById(id) == null){
            return null;
        }
        // returns link set
        return linksRepository.findById(id);
    }

    /**
     * creates link set with given values
     * @param link JSON Links object
     * @return String result of action
     */
    @ApiOperation(value = "Create a Links set with given values", response = String.class)
    @PostMapping(path = "/links")
    String createLinks(@RequestBody Links link){
        // checks if JSON is null
        if (link == null) {
            return dne;
        }
        List<Links> links = linksRepository.findAll();
        List<Movies> movies = moviesRepository.findAll();
        // checks if link set is in bounds
        if (links.size() != movies.size() - 1) {
            return mDne;
        }
        // saves the link set
        linksRepository.save(link);
        return success;
    }

    /**
     * updates a link set with given values
     * @param id link set id
     * @param request JSON Links object
     * @return updated link set
     */
    @ApiOperation(value = "Update a Links set with given values", response = Links.class)
    @PutMapping("/links/{id}")
    Links updateLinks(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id, @RequestBody Links request){
        Links link = linksRepository.findById(id);
        // checks if link set is null
        if(link == null) {
            return null;
        }
        // updates link set and returns
        request.setId(id);
        linksRepository.save(request);
        return linksRepository.findById(id);
    }

    /**
     * updates Netflix link of a given link set
     * @param id link set id
     * @param netflix Netflix link
     * @return updated link set
     */
    // individual update mappings
    @ApiOperation(value = "Update the Netflix link for a Links set", response = Links.class)
    @PutMapping(path = "/links/{id}/updateNetflix/{netflix}")
    Links updateNetflix(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id, @PathVariable("netflix") @ApiParam(name = "netflix", value = "Netflix link", example = "www.netflix.com") String netflix){
        Links l = linksRepository.findById(id);
        // checks if link set is null
        if(l == null) {
            return null;
        }
        // updates link set and returns
        l.setId(id);
        l.setNetflix(netflix);
        linksRepository.save(l);
        return linksRepository.findById(id);
    }

    /**
     * updates Hulu link of a given link set
     * @param id link set id
     * @param hulu Hulu link
     * @return updated link set
     */
    @ApiOperation(value = "Update the Hulu link for a Links set", response = Links.class)
    @PutMapping(path = "/links/{id}/updateHulu/{hulu}")
    Links updateHulu(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id, @PathVariable("hulu") @ApiParam(name = "hulu", value = "Hulu link", example = "www.hulu.com") String hulu){
        Links l = linksRepository.findById(id);
        // checks if link set is null
        if(l == null) {
            return null;
        }
        // updates link set and returns
        l.setId(id);
        l.setHulu(hulu);
        linksRepository.save(l);
        return linksRepository.findById(id);
    }

    /**
     * updates HBOMax link of a given link set
     * @param id link set id
     * @param hbomax HBOMax link
     * @return updated link set
     */
    @ApiOperation(value = "Update the HBO Max link for a Links set", response = Links.class)
    @PutMapping(path = "/links/{id}/updateHbomax/{hbomax}")
    Links updateHbomax(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id, @PathVariable("hbomax") @ApiParam(name = "hbomax", value = "HBO Max link", example = "www.play.hbomax.com") String hbomax){
        Links l = linksRepository.findById(id);
        // checks if link set is null
        if(l == null) {
            return null;
        }
        // updates link set and returns
        l.setId(id);
        l.setHboMax(hbomax);
        linksRepository.save(l);
        return linksRepository.findById(id);
    }

    /**
     * updates Disney+ link of a given link set
     * @param id link set id
     * @param disneyplus Disney+ link
     * @return updated link set
     */
    @ApiOperation(value = "Update the Disney+ link for a Links set", response = Links.class)
    @PutMapping(path = "/links/{id}/updateDisneyplus/{disneyplus}")
    Links updateDisneyplus(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id, @PathVariable("disneyplus") @ApiParam(name = "disneyplus", value = "Disney+ link", example = "www.disneyplus.com.com") String disneyplus){
        Links l = linksRepository.findById(id);
        // checks if link set is null
        if(l == null) {
            return null;
        }
        // updates link set and returns
        l.setId(id);
        l.setDisneyPlus(disneyplus);
        linksRepository.save(l);
        return linksRepository.findById(id);
    }

    /**
     * updates Amazon Prime Video link of a given link set
     * @param id link set id
     * @param amazonprime Amazon Prime Video link
     * @return updated link set
     */
    @ApiOperation(value = "Update the Amazon Prime Video link for a Links set", response = Links.class)
    @PutMapping(path = "/links/{id}/updateAmazonprime/{amazonprime}")
    Links updateAmazonprime(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id, @PathVariable("amazonprime") @ApiParam(name = "amazonprime", value = "Amazon Prime Video link", example = "www.amazon.com") String amazonprime){
        Links l = linksRepository.findById(id);
        // checks if link set is null
        if(l == null) {
            return null;
        }
        // updates link set and returns
        l.setId(id);
        l.setAmazonPrime(amazonprime);
        linksRepository.save(l);
        return linksRepository.findById(id);
    }

    /**
     * deletes link set with given id
     * @param id link set id
     * @return String result of action
     */
    @ApiOperation(value = "Deletes a Links set", response = Links.class)
    @DeleteMapping(path = "/links/{id}")
    String deleteLinks(@PathVariable("id") @ApiParam(name = "id", value = "Links set id", example = "1") int id){
        Links l = linksRepository.findById(id);
        // checks if link set exists
        if(l == null) {
            return dne;
        }
        // deletes link set
        linksRepository.deleteById(id);
        return success;
    }

}
