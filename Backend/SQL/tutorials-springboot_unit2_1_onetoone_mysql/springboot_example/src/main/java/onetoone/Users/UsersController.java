package onetoone.Users;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import onetoone.Friends.Friends;
import onetoone.Friends.FriendsRepository;
import onetoone.Movies.Movies;
import onetoone.Movies.MoviesRepository;
import onetoone.Reviews.Reviews;
import onetoone.Reviews.ReviewsRepository;
import org.apache.tomcat.util.codec.binary.Base64;
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
public class UsersController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    MoviesRepository moviesRepository;

    @Autowired
    FriendsRepository friendsRepository;

    @Autowired
    ReviewsRepository reviewsRepository;

    // return string for successful action
    private String success = "{\"message\":\"success\"}";
    // return string for failed action
    private String failure = "{\"message\":\"failure\"}";
    // return string when user doesn't exist
    private String dne = "{\"message\":\"User with Id does not exist\"}";

    /**
     * checks if movie exists
     * @param id movie id
     * @return 1 if movie exists, 0 otherwise
     */
    @ApiOperation(value = "Checks the database to see if the given movie exists", response = int.class)
    int movieExists(int id) {
        // gets all movies
        List<Movies> movies = moviesRepository.findAll();
        int flag = 0;
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == id) {
                // sets flag to 1 if mkvie exists
                flag = 1;
            }
        }
        // returns flag
        return flag;
    }

    /**
     * gets list of all users in repository
     * @return all users
     */
    @ApiOperation(value = "Gets a list of all users in the databse", response = Iterable.class)
    @GetMapping(path = "/users")
    List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    /**
     * checks if a user can log in
     * @param username username
     * @param password password
     * @return
     */
    @ApiOperation(value = "Allows the user to login and checks their username and password to get into the account", response = String.class)
    @GetMapping(path = "/users/{username}/{password}")
    String login(@PathVariable("username") @ApiParam(name = "username", value = "User username", example = "bestUser22") String username, @PathVariable("password") @ApiParam(name = "password", value = "User password", example = "1234567") String password) {
        String loginError = "{\"message\":\"Username and or password is incorrect\"}";
        // gets all users
        List<Users> users = usersRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            Users u = users.get(i);
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                String successAndID = "{\"message\":\"success\", \"id\":\"" + u.getId() + "\"}";
                // returns if username and password match
                return successAndID;
            }
        }
        // return if failure
        return loginError;
    }

    /**
     * gets profile picture of given user
     * @param id user id
     * @return profile picture
     * @throws IOException
     */
    @ApiOperation(value = "Gets the users profile ID based off their id",response = String.class)
    @GetMapping(path = "/users/{id}/pfp")
    String getPfpByID(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id) throws IOException {
        Users u = usersRepository.findById(id);
        // checks if user exists
        if(u == null) {
            return null;
        }
        // converts profile picture to Base64 encoding
        String path = u.getPfp();
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String s = Base64.encodeBase64String(bytes);
        // formats profile picture to JSON
        return "{\"pfp\":\"" + s + "\"}";
    }

    /**
     * gets user with given id
     * @param id user id
     * @return user with given id
     */
    @ApiOperation(value = "Gets a specific user from the database based off their ID", response = Users.class)
    @GetMapping(path = "/users/{id}")
    Users getUserById(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id) {
        // checks if user exists
        if(usersRepository.findById(id) == null){
            return null;
        }
        // returns user
        return usersRepository.findById(id);
    }

    /**
     * gets user with given username
     * @param username username
     * @return user with given username
     */
    @ApiOperation(value = "Gets a specific user from the database by their username", response = Users.class)
    @GetMapping(path = "/users/username/{username}")
    Users getUserByUsername(@PathVariable("username") @ApiParam(name = "username", value = "User username", example = "user3") String username) {
        // gets all users
        List<Users> users = usersRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                // returns user with matching username
                return users.get(i);
            }
        }
        // returns if user doesn't exist
        return null;
    }

    /**
     * gets user with given email
     * @param email email
     * @return user with given email
     */
    @ApiOperation(value = "Gets a user from the database by their username", response = Users.class)
    @GetMapping(path = "/users/email/{email}")
    Users getUserByEmail(@PathVariable("email") @ApiParam(name = "email", value = "User email", example = "isu@iastate.edu") String email) {
        // gets all users
        List<Users> users = usersRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                // returns if user has matching email
                return users.get(i);
            }
        }
        // returns null if user doesn't exist
        return null;
    }

    /**
     * gets all users with given favorite movie id
     * @param movieID movie id
     * @return all users with given favorite movie id
     */
    @ApiOperation(value = "Gets a user by their favorite movie ID", response = Users.class)
    @GetMapping(path = "/users/favMID/{movieID}")
    List<Users> getUserByMovieID(@PathVariable("movieID") @ApiParam(name = "movieID", value = "User movieID", example = "22") int movieID) {
        // gets all users
        List<Users> users = usersRepository.findAll();
        List<Users> u = new ArrayList<Users>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getFavMID() == movieID) {
                // adds user to list if favorite movie id matches
                u.add(users.get(i));
            }
        }
        // returns the list
        return u;
    }

    /**
     * gets all users with given user permission type
     * @param usertype user permission type
     * @return all users with given user permission type
     */
    @ApiOperation(value = "Gets users by a specific user type", response = Iterable.class)
    @GetMapping(path = "/users/usertype/{usertype}")
    List<Users> getUserByUserType(@PathVariable("usertype") @ApiParam(name = "usertype", value = "User usertype", example = "Admin") String usertype) {
        // gets all users
        List<Users> users = usersRepository.findAll();
        List<Users> user = new ArrayList<Users>();
        String ut = usertype.toLowerCase();
        for (int i = 0; i < users.size(); i++) {
            String u = users.get(i).getUserType().toLowerCase();
            if (u.equals(ut)) {
                // adds user to list if user permission type matches
                user.add(users.get(i));
            }
        }
        // returns the list
        return user;
    }

    /**
     * gets users with given profile picture
     * @param pfp profile picture
     * @return users with given profile picture
     */
    @ApiOperation(value = "Gets users by their profile picture in the database", response = Iterable.class)
    @GetMapping(path = "/users/pfp/{pfp}")
    List<Users> getUsersByPFP(@PathVariable("pfp") @ApiParam(name = "pfp", value = "User pfp", example = "images/guestpfp") String pfp) {
        // gets all users
        List<Users> users = usersRepository.findAll();
        List<Users> u = new ArrayList<Users>();
        for(int i = 0; i < users.size(); i++) {
            if (users.get(i).getPfp().equals(pfp)) {
                // adds user to list if profile picture matches
                u.add(users.get(i));
            }
        }
        // returns the list
        return u;
    }

    /**
     * creates a user with given values, used for sign up
     * @param users JSON Users object
     * @return String result of action
     */
    @ApiOperation(value = "Creates a user with the given criteria and checks if the any usernam or email is already used", response = String.class)
    @PostMapping(path = "/users")
    String createUser(@RequestBody Users users) {
        String usedUsername = "{\"message\":\"Username is already in use\"}";
        String usedEmail = "{\"message\":\"Email is already in use\"}";
        String usedBoth = "{\"message\":\"Username and Email are already in use\"}";
        String userT = "{\"message\":\"UserType is not valid\"}";
        // gets all users
        List<Users> allUsers = usersRepository.findAll();
        for (int i = 0; i < allUsers.size(); i++) {
            Users u = allUsers.get(i);
            // checks if username and email are available
            if (u.getUsername().equals(users.getUsername()) && u.getEmail().equals(users.getEmail())) {
                return usedBoth;
            } else if (u.getUsername().equals(users.getUsername())){
                return usedUsername;
            }else if(u.getEmail().equals(users.getEmail())){
                return usedEmail;
            }
        }
        // checks if usertype is valid
        if (!(users.getUserType().equals("Guest") || users.getUserType().equals("Admin")
                || users.getUserType().equals("User") || users.getUserType().equals("guest")
                || users.getUserType().equals("admin") || users.getUserType().equals("user"))) {
            return userT;
        }
        // saves user and returns
        usersRepository.save(users);
        return success;
    }

    /**
     * uodates a user with given values
     * @param id user id
     * @param request Users JSON object
     * @return updated user
     */
    @ApiOperation(value = "Updates a user with the new given criteria", response = Users.class)
    @PutMapping("/users/{id}")
    Users updateUser(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @RequestBody Users request) {
        // checks if user exists
        Users users = usersRepository.findById(id);
        if (users == null) {
            return null;
        }
        // gets all users
        List<Users> allUsers = usersRepository.findAll();
        // checks if username or email is already taken
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUsername().equals(request.getUsername())) {
                return null;
            } else if (allUsers.get(i).getEmail().equals(request.getEmail())) {
                return null;
            }
        }
        // checks if usertype is valid
        if (!(request.getUserType().equals("Guest") || request.getUserType().equals("Admin")
                || request.getUserType().equals("User") || request.getUserType().equals("guest")
                || request.getUserType().equals("admin") || request.getUserType().equals("user"))) {
            return null;
        }
        // saves user and returns
        request.setId(id);
        usersRepository.save(request);
        return usersRepository.findById(id);
    }

    // individual update mappings

    /**
     * updates username for given user
     * @param id user id
     * @param username username
     * @return updated user
     */
    @ApiOperation(value = "Updates a users username with a new one if it doesnt exist", response = Users.class)
    @PutMapping("/users/{id}/updateUsername/{username}")
    Users updateUsername(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("username") @ApiParam(name = "username", value = "User username", example = "user4") String username){
        Users u = usersRepository.findById(id);
        // checks if user exists
        if(u == null) {
            return null;
        }
        // gets all users
        List<Users> allUsers = usersRepository.findAll();
        // checks if username is already taken
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUsername().equals(username)) {
                return null;
            }
        }
        // saves user and returns
        u.setId(id);
        u.setUsername(username);
        usersRepository.save(u);
        return usersRepository.findById(id);
    }

    /**
     * updates email of a given user
     * @param id user id
     * @param email email
     * @return updated user
     */
    @ApiOperation(value = "Updates a users email as long as it doesnt exist", response = Users.class)
    @PutMapping("/users/{id}/updateEmail/{email}")
    Users updateEmail(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("email") @ApiParam(name = "email", value = "User email", example = "isu@iastate.edu") String email){
        Users u = usersRepository.findById(id);
        // checks if user exists
        if(u == null) {
            return null;
        }
        // gets all suers
        List<Users> allUsers = usersRepository.findAll();
        // checks if email is already taken
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getEmail().equals(email)) {
                return null;
            }
        }
        // saves user and returns
        u.setId(id);
        u.setEmail(email);
        usersRepository.save(u);
        return usersRepository.findById(id);
    }

    /**
     * updates user permission type for given user
     * @param id user id
     * @param usertype user permission type
     * @return updated user
     */
    @ApiOperation(value = "Updates the type of user on an existing user", response = Users.class)
    @PutMapping("/users/{id}/updateUsertype/{usertype}")
    Users updateUserType(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("userType") @ApiParam(name = "userType", value = "User userType", example = "guest") String usertype){
        Users u = usersRepository.findById(id);
        // checks if user exists
        if(u == null) {
            return null;
        }
        // checks if usertype is valid
        if (!(usertype.equals("Guest") || usertype.equals("Admin") || usertype.equals("User")
                || usertype.equals("guest") || usertype.equals("admin") || usertype.equals("user"))) {
            return null;
        }
        // saves user and returns
        u.setId(id);
        u.setUserType(usertype);
        usersRepository.save(u);
        return usersRepository.findById(id);
    }

    /**
     * updates password for given user
     * @param id user id
     * @param password password
     * @return updated user
     */
    @ApiOperation(value = "Updates the password of a given user in the database", response = Users.class)
    @PutMapping("/users/{id}/updatePassword/{password}")
    Users updatePassword(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("password") @ApiParam(name = "password", value = "User password", example = "123456") String password){
        Users u = usersRepository.findById(id);
        // checks if user exists
        if(u == null) {
            return null;
        }
        // saves user and returns
        u.setId(id);
        u.setPassword(password);
        usersRepository.save(u);
        return usersRepository.findById(id);
    }

    /**
     * updates favorite movie id for given user
     * @param id user id
     * @param movieid favorite movie id
     * @return
     */
    @ApiOperation(value = "Updates a users favorite movie id", response = Users.class)
    @PutMapping("/users/{id}/updateMovie/{movieid}")
    Users updateMovieid(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("movieid") @ApiParam(name = "movieid", value = "User movieid", example = "1") int movieid){
        // checks if movie exists
        int flag = movieExists(movieid);
        if (flag == 0) {
            return null;
        }
        // checks if user exists
        Users u = usersRepository.findById(id);
        if(u == null) {
            return null;
        }
        // updates user and returns
        u.setId(id);
        u.setFavMID(movieid);
        usersRepository.save(u);
        return usersRepository.findById(id);
    }

    /**
     * updates profile picture for given user
     * @param id user id
     * @param pfp profile picture
     * @return updates user
     */
    @ApiOperation(value = "Updates a users profile picture", response = Users.class)
    @PutMapping("/users/{id}/updatePfp/{pfp}")
    Users updatePfp(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("pfp") @ApiParam(name = "pfp", value = "User pfp", example = "images/guestpfp") String pfp){
        Users u = usersRepository.findById(id);
        // checks if user exists
        if(u == null) {
            return null;
        }
        // updates user and returns
        u.setId(id);
        u.setPfp("/Images/PFPs/" + pfp);
        usersRepository.save(u);
        return usersRepository.findById(id);
    }

    /**
     * deletes user with given is
     * @param id user id
     * @return String result of action
     */
    @ApiOperation(value = "Deletes a user by their IDand their friendships", response = String.class)
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id) {
        Users users = usersRepository.findById(id);
        // check is user exists
        if (users == null) {
            return dne;
        }

        // deletes associated reviews
        List<Reviews> reviews = reviewsRepository.findAll();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getUserid() == id) {
                reviewsRepository.deleteById(reviews.get(i).getId());
            }
        }

        List<Friends> user = new ArrayList<Friends>();
        List<Friends> friends = friendsRepository.findAll();
        for(int i = 0; i < friends.size(); i++){
            // creates a list of friends of the suer
            if(friends.get(i).getUser1() == id) {
                user.add(friends.get(i));
            }else if(friends.get(i).getUser2() == id){
                user.add(friends.get(i));
            }
        }
        // deletes friends of the user
        for(int i = 0; i < user.size(); i++){
            friendsRepository.deleteById(user.get(i).getId());
        }
        usersRepository.deleteById(id);
        List<Users> allUsers = usersRepository.findAll();
        for (int i = 0; i < allUsers.size(); i++) {
            Users u = allUsers.get(i);
            // switches to guest user
            if (u.getUserType().equals("Guest") || u.getUsername().equals("guest")) {
                String guestUser = "{\"message\":\" User with Id " + id + " deleted\", \"Guest\":\"" + u.getId() + "\"}";
                return guestUser;
            }
        }
        return failure;
    }

}
