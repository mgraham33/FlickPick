package onetoone.Reviews;

import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import onetoone.Movies.Movies;
import onetoone.Movies.MoviesRepository;
import onetoone.Users.Users;
import onetoone.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Chris Costa and Matt Graham
 */

@RestController
@Api(value = "Swagger2DemoRestController")
public class ReviewsController {

    @Autowired
    ReviewsRepository reviewsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    MoviesRepository moviesRepository;

    // return string for successful action
    private String success = "{\"message\":\"success\"}";
    // return string for action when review doesn't exist
    private String dne = "{\"message\":\"Review with Id does not exist\"}";
    // return string for action when user doesn't exist
    private String userDNE = "{\"message\":\"User does not exist\"}";
    // return string for action when movie doesn't exist
    private String movieDNE = "{\"message\":\"Movie does not exist\"}";
    // return string for action when rating is out of bounds
    private String obRating = "{\"message\":\"Rating is out of the given range\"}";

    /**
     * checks if user exists
     * @param id user id
     * @return 1 if user exists, 0 otherwise
     */
    @ApiOperation(value = "Checks to see if a specific user exists in the database", response = int.class)
    int userExists(int id) {
        // gets all users
        List<Users> users = usersRepository.findAll();
        int flag = 0;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                // sets flag to 1 if user exists
                flag = 1;
            }
        }
        // returns flag
        return flag;
    }

    /**
     * checks if movie exists
     * @param id movie id
     * @return 1 if movie exists, 0 otherwise
     */
    @ApiOperation(value = "Checks to see if a specific movie exists in the database using the ID of the movie", response =  int.class)
    int movieExists(int id) {
        // gets all movies
        List<Movies> movies = moviesRepository.findAll();
        int flag = 0;
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == id) {
                // sets flag to 1 if movie exists
                flag = 1;
            }
        }
        // returns flag
        return flag;
    }

    /**
     * updates the rating of a given review
     * @param id review id
     */
    @ApiOperation(value = "Updates the rating on a movie in the database based off the movie ID", response = void.class)
    void updateMovieRating(int id) {
        // gets all reviews
        List<Reviews> r = reviewsRepository.findAll();
        double rating = 0;
        int count = 0;
        // adds all ratings for the same movie
        for (int i = 0; i < r.size(); i++) {
            if (r.get(i).getMovieid() == id) {
                rating += r.get(i).getRating();
                count++;
            }
        }
        // averages the rating
        if (count == 0) {
            rating = 0;
        } else {
            rating /= count;
        }
        // normalizes the rating within range
        if (rating > 5) {
            rating = 5;
        } else if (rating < 0) {
            rating = 0;
        }
        // updates the rating
        Movies m = moviesRepository.findById(id);
        m.setId(id);
        m.setRating(rating);
        moviesRepository.save(m);
    }

    /**
     * gets all reviews in the repository
     * @return all reviews
     */
    @ApiOperation(value = "Gets all the reviews that exist in the database", response = Iterable.class)
    @GetMapping(path = "/reviews")
    List<Reviews> getAllReviews(){
        return reviewsRepository.findAll();
    }

    /**
     * gets all reviews with a given movie id
     * @param movieID movie id
     * @return all reviews with given movie id
     */
    @ApiOperation(value = "Gets all the reviews for a specific movie in the database by the movieID", response = Iterable.class)
    @GetMapping(path = "/reviews/movie/{movieID}")
    List<Reviews> getAllReviewsByMID(@PathVariable("movieID") @ApiParam(name = "movieID", value = "Movie id", example = "1") int movieID) {
        int exists = movieExists(movieID);
        // checks if movie exists
        if(exists == 0){
            return null;
        }
        // gets all reviews
        List<Reviews> reviews = reviewsRepository.findAll();
        List<Reviews> review = new ArrayList<Reviews>();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getMovieid() == movieID) {
                // adds the review to the lis if the movie id matches
                review.add(reviews.get(i));
            }
        }
        // returns the list
        return review;
    }

    /**
     * gets a review with a given id
     * @param id review id
     * @return review with given id
     */
    @ApiOperation(value = "Gets a specific review in the database based off the review id", response = Reviews.class)
    @GetMapping(path = "/reviews/{id}")
    Reviews getReviewById(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id){
        if(reviewsRepository.findById(id) == null){
            // checks if the review is null
            return null;
        }
        // returns the review
        return reviewsRepository.findById(id);
    }

    /**
     * gets all reviews with given user id
     * @param id user id
     * @return all reviews with given user id
     */
    @ApiOperation(value = "Gets all the reviews in the database that a specific user has made with their userID", response = Iterable.class)
    @GetMapping(path = "/reviews/user/{id}")
    List<Reviews> getSpecificReviewsByUserId(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id){
        int exists = userExists(id);
        // checks if user exists
        if(exists == 0){
            return null;
        }
        // gets all reviews
        List<Reviews> userReviews = new ArrayList<>();
        List<Reviews> reviews = reviewsRepository.findAll();
        for(int i = 0; i < reviews.size(); i++){
            if(reviews.get(i).getUserid() == id){
                // adds review to list if user id matches
                userReviews.add(reviews.get(i));
            }
        }
        // returns the list
        return userReviews;
    }

    /**
     * gets all reviews with a given username
     * @param username username
     * @return all reviews with a given username
     */
    @ApiOperation(value = "Gets all reviews in the database that was made by a specific user by username", response = Iterable.class)
    @GetMapping(path = "/reviews/username/{username}")
    List<Reviews> getSpecificReviewsByUsername(@PathVariable("username") @ApiParam(name = "username", value = "Movie userUsername", example = "guest1") String username){
        List<Users> users = usersRepository.findAll();
        int flag = 0;
        // checks if username exists
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                flag = 1;
            }
        }
        // returns null if user does not exist
        if(flag == 0){
            return null;
        }
        // get all reviews
        List<Reviews> userReviews = new ArrayList<>();
        List<Reviews> reviews = reviewsRepository.findAll();
        for(int i = 0; i < reviews.size(); i++){
           if(usersRepository.findById(reviews.get(i).getUserid()).getUsername().equals(username)){
               // adds the review to the list if the username matches
               userReviews.add(reviews.get(i));
           }
        }
        // returns the list
        return userReviews;
    }

    /**
     * creates a review with given values
     * @param reviews JSON Reviews object
     * @return String result of action
     */
    @ApiOperation(value = "Allows a user to create a review with the given values", response = String.class)
    @PostMapping(path = "/reviews")
    String createReview(@RequestBody Reviews reviews){
        // checks if JSON is null
        if (reviews == null) {
            return dne;
        }
        // checks if user or movie exist
        int flag = userExists(reviews.getUserid());
        int flag2 = movieExists(reviews.getMovieid());
        if (flag == 0) {
            return userDNE;
        }
        if(flag2 == 0) {
            return movieDNE;
        }
        // check if the rating is in range
        if (reviews.getRating() > 5 || reviews.getRating() < 0) {
            return obRating;
        }
        // saves review and returns
        reviewsRepository.save(reviews);
        updateMovieRating(reviews.getMovieid());
        return success;
    }

    /**
     * updates a review with given values
     * @param id review id
     * @param request JSON Reviews object
     * @return updated review
     */
    @ApiOperation(value = "Updates a review with the given values", response = Reviews.class)
    @PutMapping(path = "/reviews/{id}")
    Reviews updateReview(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id, @RequestBody Reviews request){
        Reviews reviews = reviewsRepository.findById(id);
        // checks if JSON is null
        if(reviews == null) {
            return null;
        }
        // checks if user or movie exists
        int flag = userExists(request.getUserid());
        int flag2 = movieExists(request.getMovieid());
        if (flag == 0 || flag2 == 0) {
            return null;
        }
        // check if the rating is in range
        if (request.getRating() > 5 || request.getRating() < 0) {
            return null;
        }
        // saves review and returns
        request.setId(id);
        reviewsRepository.save(request);
        updateMovieRating(reviews.getMovieid());
        return reviewsRepository.findById(id);
    }

    /**
     * updates rating for a given review
     * @param id review id
     * @param rating rating
     * @return updated review
     */
    @ApiOperation(value = "Updates a rating for a movie with a new given rating", response = Reviews.class)
    @PutMapping(path = "/reviews/{id}/updateRating/{rating}")
    Reviews updateRating(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id, @PathVariable("rating") @ApiParam(name = "rating", value = "Movie rating", example = "5") double rating){
        // checks if review exists
        Reviews r = reviewsRepository.findById(id);
        if(r == null) {
            return null;
        }
        // check if the rating is in range
        if (rating > 5 || rating < 0) {
            return null;
        }
        // updates review and returns
        r.setId(id);
        r.setRating(rating);
        reviewsRepository.save(r);
        // recalculates rating
        updateMovieRating(r.getMovieid());
        return reviewsRepository.findById(id);
    }

    /**
     * updates movie id for given review
     * @param id
     * @param movieid
     * @return
     */
    @ApiOperation(value = "Updates the movie id and rating with the new criteria", response = Reviews.class)
    @PutMapping(path = "/reviews/{id}/updateMovieid/{movieid}")
    Reviews updateMovieid(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id, @PathVariable("movieid") @ApiParam(name = "movieid", value = "Movie id", example = "1") int movieid){
        // checks if movie exists
        int flag = movieExists(movieid);
        if (flag == 0) {
            return null;
        }
        // checks if review exists
        Reviews r = reviewsRepository.findById(id);
        if(r == null) {
            return null;
        }
        // saves review and returns
        int oldID = reviewsRepository.findById(id).getMovieid();
        r.setId(id);
        r.setMovieid(movieid);
        reviewsRepository.save(r);
        // re-calculates ratings
        updateMovieRating(oldID);
        updateMovieRating(movieid);
        return reviewsRepository.findById(id);
    }

    /**
     * updates user id for a given review
     * @param id review id
     * @param userid user id
     * @return updated review
     */
    @ApiOperation(value = "Updates the ratings and user id to a new one", response = Reviews.class)
    @PutMapping(path = "/reviews/{id}/updateUserid/{userid}")
    Reviews updateUserid(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id, @PathVariable("userid") @ApiParam(name = "userid", value = "Movie userid", example = "4") int userid){
        // checks if user exists
        int flag = userExists(userid);
        if (flag == 0) {
            return null;
        }
        // checks if review exists
        Reviews r = reviewsRepository.findById(id);
        if(r == null) {
            return null;
        }
        // saves review and returns
        r.setId(id);
        r.setUserid(userid);
        reviewsRepository.save(r);
        return reviewsRepository.findById(id);
    }

    /**
     * updates the reason for a given review
     * @param id review id
     * @param reason description
     * @return updated review
     */
    @ApiOperation(value = "Updates the reason with the new given reason", response = Reviews.class)
    @PutMapping(path = "/reviews/{id}/updateReason/{reason}")
    Reviews updateReason(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id, @PathVariable("reason") @ApiParam(name = "reason", value = "Movie reason", example = "was a good movie") String reason){
        Reviews r = reviewsRepository.findById(id);
        // checks if review exists
        if(r == null) {
            return null;
        }
        r.setId(id);
        r.setReason(reason);
        reviewsRepository.save(r);
        return reviewsRepository.findById(id);
    }

    /**
     * deletes given review
     * @param id review id
     * @return String result of action
     */
    @ApiOperation(value = "Deletes the review in the database based off the reviewID", response = String.class)
    @DeleteMapping(path = "/reviews/{id}")
    String deleteReview(@PathVariable("id") @ApiParam(name = "id", value = "Review id", example = "1") int id){
        Reviews r = reviewsRepository.findById(id);
        // checks if review exists
        if(r == null) {
            return dne;
        }
        // deletes review
        reviewsRepository.deleteById(id);
        return success;
    }
}
