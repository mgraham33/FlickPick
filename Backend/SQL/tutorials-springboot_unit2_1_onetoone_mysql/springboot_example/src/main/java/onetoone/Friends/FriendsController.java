package onetoone.Friends;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import onetoone.Users.Users;
import onetoone.Users.UsersRepository;
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
public class FriendsController {
    @Autowired
    FriendsRepository friendsRepository;
    @Autowired
    UsersRepository usersRepository;

    // return message for successful action
    private String success = "{\"message\":\"success\"}";
    // return message for failed action
    private String failure = "{\"message\":\"failure\"}";
    // return message for action where the friendship does not exist
    private String dne = "{\"message\":\"Friendship with Id does not exist\"}";
    // return message for action where the user does not exist
    private String userDNE = "{\"message\":\"User does not exist\"}";

    /**
     * gets all friendships in the repository
     * @return all friendships in the repository
     */
    @ApiOperation(value = "Gets a list of all friends that exists in the database", response = Iterable.class)
    @GetMapping(path = "/friends")
    List<Friends> getAllFriends(){
        return friendsRepository.findAll();
    }

    /**
     * checks if a user exists in the repository
     * @param id id of the user
     * @return 1 if the user exists, 0 otherwise
     */
    @ApiOperation(value = "Checks to see if a specific user exists in the database based of the userID", response = int.class)
    int userExists(int id) {
        // gets all users in the repository
        List<Users> users = usersRepository.findAll();
        // sets flag to 0 by default
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
     * gets all friendships that exists involving a specified user
     * @param id id of specified user
     * @return list of all friendships involving a specified user
     */
    @ApiOperation(value = "Gets a list of all friends that exists in the databse based of the id", response = String.class)
    @GetMapping(path = "/friends/list/{id}")
    String getFriendListById(@PathVariable("id") @ApiParam(name = "id", value = "Friends id", example = "1") int id){
        // checks if user exists
        int flag = userExists(id);
        // returns if user does not exist
        if (flag == 0) {
            return userDNE;
        }
        List<Friends> user = new ArrayList<Friends>();
        // gets all friendships in the repository
        List<Friends> friends = friendsRepository.findAll();
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getUser1() == id) {
                // adds the friendship to the list if user 1 is the specified user
                user.add(friends.get(i));
            }else if(friends.get(i).getUser2() == id){
                // adds the friendship to the list if user 2 is the specified user
                user.add(friends.get(i));
            }
        }
        String friendList = "[";
        for (int i = 0; i < user.size(); i++) {
            // formats the list into JSON
            friendList += "{\"id\":\"" + user.get(i).getId() + "\", \"user1\":\"" + user.get(i).getUser1() + "\", \"user2\":\"" + user.get(i).getUser2()
                    + "\", \"friend_type\":\"" + user.get(i).getFriendtype() + "\"}";
            if (i + 1 < user.size()) {
                friendList += ",";
            }
        }
        friendList += "]";
        // returns the list
        return friendList;
    }

    /**
     * gets the profile picture of the first user in a friendship
     * @param id id of the friendship
     * @return profile picture of the first user
     * @throws IOException
     */
    @ApiOperation(value = "Gets the profile picture for user 1 in a friendship", response = String.class)
    @GetMapping(path = "/friends/{id}/pfp/user1")
    String getPfpByIDFriendsUser1(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id) throws IOException {
        // checks if the friendship exists
        Friends f = friendsRepository.findById(id);
        if(f == null){
            return null;
        }
        // checks if the user exists
        Users u1 = usersRepository.findById(f.getUser1());

        // formats the profile picture into Base64 encoding
        String path = u1.getPfp();
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String s = Base64.encodeBase64String(bytes);
        // returns the encoded String in JSON
        return "[{\"pfp\":\"" + s + "\"}]";
    }

    /**
     * gets the profile picture of the second user in a friendship
     * @param id id of the friendship
     * @return profile picture of the second user
     * @throws IOException
     */
    @ApiOperation(value = "gets the profile picture for user 2 in the friendship", response = String.class)
    @GetMapping(path = "/friends/{id}/pfp/user2")
    String getPfpByIDFriendsUser2(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id) throws IOException {
        // checks if the friendship exists
        Friends f = friendsRepository.findById(id);
        if(f == null){
            return null;
        }
        // checks if the user exists
        Users u2 = usersRepository.findById(f.getUser2());

        // converts the profile picture to Base64 encoding
        String path = u2.getPfp();
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String s = Base64.encodeBase64String(bytes);
        // returns the profile picture in JSON format
        return "[{\"pfp\":\"" + s + "\"}]";
    }

    /**
     * gets a friendship by id
     * @param id id of the friendship
     * @return friendship with given id
     */
    @ApiOperation(value = "gets friends by a specific friend id", response = Friends.class)
    @GetMapping(path = "/friends/{id}")
    Friends getFriendsById(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id){
        // checks if the friendship exists
        if(friendsRepository.findById(id) == null){
            return null;
        }
        // returns the friendship
        return friendsRepository.findById(id);
    }

    /**
     * gets friendships with a given friend type
     * @param friendType specified friend type
     * @return friendships with given friend type
     */
    @ApiOperation(value = "Gets the friends based of the type of friend they are", response = Iterable.class)
    @GetMapping(path = "/friends/friendType/{friendType}")
    List<Friends> getFriendsByFriendType(@PathVariable("friendType") @ApiParam(name = "friendType", value = "Friends friendType", example = "requested") String friendType){
        // gets all friendships
        List<Friends> friends = friendsRepository.findAll();
        List<Friends> friend = new ArrayList<Friends>();
        // converts friend type to lower case
        String ut = friendType.toLowerCase();
        for (int i = 0; i < friends.size(); i++) {
            String u = friends.get(i).getFriendtype().toLowerCase();
            // checks if the friend type and parameter are equivalent
            if (u.equals(ut)) {
                // adds the friendship to the list
                friend.add(friends.get(i));
            }
        }
        // returns the list
        return friend;
    }

    /**
     * gets a friend of a user with a given username
     * @param id id of the user
     * @param username username of the desired user
     * @return friend of the user with a given username
     */
    @ApiOperation(value = "Gets friends of a user by searching with the username", response = Friends.class)
    @GetMapping(path = "/friends/{id}/username/{username}")
    Friends getSpecificFriendsByUsername(@PathVariable("id") @ApiParam(name = "id", value = "User id", example = "1") int id, @PathVariable("username") @ApiParam(name = "username", value = "Friends username", example = "bestUser2") String username){
        // checks if user exists
        int flag = userExists(id);
        if (flag == 0) {
            return null;
        }
        List<Friends> user = new ArrayList<>();
        // gets all friendships
        List<Friends> friends = friendsRepository.findAll();
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getUser1() == id || friends.get(i).getUser2() == id) {
                // adds the friendship to the list if the user is a part of it
                user.add(friends.get(i));
            }
        }
        for(int i = 0; i < user.size(); i++){
            if(usersRepository.findById(user.get(i).getUser1()).getUsername().equals(username)
                    || usersRepository.findById(user.get(i).getUser2()).getUsername().equals(username)){
                // returns the friendship if the username is present
                return user.get(i);
            }
        }
        // returns null if user is not found
        return null;
    }

    /**
     * creates a friendship with given values
     * @param friend JSON Friends object
     * @return String result of action
     */
    @ApiOperation(value = "Allows creating friends with the given values", response = String.class)
    @PostMapping(path = "/friends")
    String createFriends(@RequestBody Friends friend){
        String sameUser = "{\"message\":\"A user cannot be friends with itself.\"}";
        int flag = userExists(friend.getUser1());
        int flag2 = userExists(friend.getUser2());
        // checks if both users exist
        if (flag == 0 || flag2 == 0) {
            return null;
        }
        // checks if JSON is empty and returns
        if (friend == null) {
            return dne;
        }
        // check if both users are te same and returns
        if(friend.getUser1() == friend.getUser2()){
            return sameUser;
        }
        int id1 = friend.getUser1();
        int id2 = friend.getUser2();
        // gets all friendships
        List<Friends> friends = friendsRepository.findAll();

        for(int i = 0; i < friends.size(); i++){
            if((friends.get(i).getUser1() == id1 && friends.get(i).getUser2() == id2) && (friends.get(i).getUser2()== id1 || friends.get(i).getUser1() == id2)){
                // returns if friendship already exists
                return failure;
            }
        }
        // checks if friendship type is valid
        String fType = friend.getFriendtype().toLowerCase();
        if(fType.equals("accepted") || fType.equals("requested") || fType.equals("blocked")) {
            // saves the friendship
            friendsRepository.save(friend);
        } else {
            // returns if friendship type is invalid
            return failure;
        }
        // returns when complete
        return success;
    }

    /**
     * updates a friendship with given values
     * @param id id of the friendship
     * @param request JSON Friends object
     * @return the updated friendship
     */
    @ApiOperation(value = "Allows to update friends in the database with the new given information", response = Friends.class)
    @PutMapping("/friends/{id}")
    Friends updateFriends(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id, @RequestBody Friends request){
        // checks if users exist
        int flag = userExists(request.getUser1());
        int flag2 = userExists(request.getUser2());
        // returns null if either user doesn't exist
        if (flag == 0 || flag2 == 0) {
            return null;
        }
        // returns null if the friendship doesn't exist
        Friends friend = friendsRepository.findById(id);
        if(friend == null) {
            return null;
        }
        String fType = request.getFriendtype().toLowerCase();
        if(fType.equals("unblocked")){
            if(friendsRepository.findById(id).getFriendtype().equals("blocked")){
                if (request.getUser1() == friendsRepository.findById(id).getUser1()) {
                    // unblocks a user if the user who performed the block decides to unblock
                    friendsRepository.deleteById(id);
                    return null;
                }
            } else{
                // returns null if the blocked user attempts an unblock
                return null;
            }
        }
        // returns null if the friend type is invalid
        if(!(fType.equals("accepted") || fType.equals("requested") || fType.equals("rejected") || fType.equals("blocked"))){
            return null;
        }
        if(fType.equals("rejected")){
            // deletes the friendship if a request is rejected
            friendsRepository.deleteById(id);
            return null;
        }
        request.setId(id);
        // saves the updated user and returns it
        friendsRepository.save(request);
        return friendsRepository.findById(id);
    }

    /**
     * updates the friend type of specified friendship
     * @param id id of friendship
     * @param friendType friendship type
     * @return updated friendship
     */
    @ApiOperation(value = "Updates the friend type with the given information", response = Friends.class)
    @PutMapping("/friends/{id}/UpdateFriendsType/{friendType}")
    Friends updateFriendType(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id, @PathVariable("friendType") @ApiParam(name = "friendType", value = "Friends friendType", example = "accepted") String friendType){
        Friends f = friendsRepository.findById(id);
        if(f == null) {
            // returns null if the friendship doesn't exist
            return null;
        }
        String fType = friendType.toLowerCase();
        // checks if the previous friend type was blocked before unblocking
        if(fType.equals("unblocked")){
            if(friendsRepository.findById(id).getFriendtype().equals("blocked")){
                // deletes the blocked friendship
                friendsRepository.deleteById(id);
                return null;
            }
        }
        if(!(fType.equals("accepted") || fType.equals("requested") || fType.equals("rejected") || fType.equals("blocked"))){
            // returns null if friend type is invalid
            return null;
        }
        // deletes a requested frienship when rejected
        if(fType.equals("rejected")){
            friendsRepository.deleteById(id);
            return null;
        }
        // saves the updated friendship and returns
        f.setId(id);
        f.setFriend_type(fType);
        friendsRepository.save(f);
        return friendsRepository.findById(id);
    }

    // individual update mappings

    /**
     * updates the first user in a friendship
     * @param id id of the friendship
     * @param user1 id of the first user
     * @return updated friendship
     */
    @ApiOperation(value = "Updates the first user in the friendship with the new given information", response = Friends.class)
    @PutMapping("/friends/{id}/updateUser1/{user1}")
    Friends updateUser1(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id, @PathVariable("user1") @ApiParam(name = "user1", value = "Friends user1", example = "1") int user1){
        // checks if user exists
        int flag = userExists(user1);
        if (flag == 0) {
            return null;
        }
        // checks if the friendship exists
        Friends f = friendsRepository.findById(id);
        if(f == null) {
            return null;
        }
        // saves the friendship and returns
        f.setId(id);
        f.setUser1(user1);
        friendsRepository.save(f);
        return friendsRepository.findById(id);
    }

    /**
     * updates the second user in a friendship
     * @param id id of the friendship
     * @param user2 id of the second user
     * @return updated friendship
     */
    @ApiOperation(value = "Updates the second user in the friendship with the new given information", response = Friends.class)
    @PutMapping("/friends/{id}/updateUser2/{user2}")
    Friends updateUser2(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id, @PathVariable("user2") @ApiParam(name = "user2", value = "Friends user2", example = "2") int user2){
        // checks if user exists
        int flag = userExists(user2);
        if (flag == 0) {
            return null;
        }
        // checks if the frienship exists
        Friends f = friendsRepository.findById(id);
        if(f == null) {
            return null;
        }
        // saves the friendship and returns
        f.setId(id);
        f.setUser2(user2);
        friendsRepository.save(f);
        return friendsRepository.findById(id);
    }

    /**
     * deletes all friendships involving a user
     * @param id id of the user
     * @return String result of the action
     */
    @ApiOperation(value = "Deletes the friends list by the given id", response = String.class)
    @DeleteMapping(path = "/friends/list/delete/{id}")
    String deleteFriendListById(@PathVariable("id") @ApiParam(name = "id", value = "user id", example = "1") int id){
        // checks if user exists
        int flag = userExists(id);
        if (flag == 0) {
            return userDNE;
        }
        List<Friends> user = new ArrayList<Friends>();
        // gets all friendships
        List<Friends> friends = friendsRepository.findAll();
        for(int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getUser1() == id || friends.get(i).getUser2() == id) {
                // adds the friendship to the list if the user is part of it
                user.add(friends.get(i));
            }
        }
        // deletes all friendships in the list
        for (int i = 0; i < user.size(); i++) {
            friendsRepository.deleteById(user.get(i).getId());
        }
        // returns when action is completed
        return success;
    }

    /**
     * deletes a friendship with a given id
     * @param id id of friendship
     * @return String result of action
     */
    @ApiOperation(value = "Deletes a friendship based off the given ID", response = String.class)
    @DeleteMapping(path = "/friends/{id}")
    String deleteFriends(@PathVariable("id") @ApiParam(name = "id", value = "Friend id", example = "1") int id){
        Friends f = friendsRepository.findById(id);
        // checks if friendship exists
        if (f == null) {
            return dne;
        }
        // deletes friendship
        friendsRepository.deleteById(id);
        return success;
    }

}
