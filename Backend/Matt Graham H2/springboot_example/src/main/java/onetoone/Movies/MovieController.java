package onetoone.Movies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Users.User;
import onetoone.Users.UserRepository;

@RestController
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/movies")
    List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    @GetMapping(path = "/movies/{id}")
    Movie getMovieById(@PathVariable int id){
        return movieRepository.findById(id);
    }

    @PostMapping(path = "/movies")
    String createMovie(@RequestBody Movie Movie){
        if (Movie == null)
            return failure;
        movieRepository.save(Movie);
        return success;
    }

    @PutMapping(path = "/movies/{id}")
    Movie updateMovie(@PathVariable int id, @RequestBody Movie request){
        Movie movie = movieRepository.findById(id);
        if(movie == null)
            return null;
        movieRepository.save(request);
        return movieRepository.findById(id);
    }

    @DeleteMapping(path = "/movies/{id}")
    String deleteMovie(@PathVariable int id){

        // Check if there is an object depending on user and then remove the dependency
        User user = userRepository.findByMovie_Id(id);
        user.setMovie(null);
        userRepository.save(user);

        // delete the laptop if the changes have not been reflected by the above statement
        movieRepository.deleteById(id);
        return success;
    }
}
