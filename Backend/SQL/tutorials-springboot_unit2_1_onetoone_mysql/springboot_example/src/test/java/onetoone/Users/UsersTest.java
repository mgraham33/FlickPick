package onetoone.Users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    Users u1 = new Users("User1", "user1234@iastate.edu", "user", "U$er123", 2, "img/userimg");
    Users u2 = new Users();

    @Test
    void getId() {
        assertEquals(u1.getId(), 0);
    }

    @Test
    void setId() {
        u1.setId(200);
        assertEquals(u1.getId(), 200);
    }

    @Test
    void getUsername() {
        assertEquals(u1.getUsername(), "User1");
    }

    @Test
    void setUsername() {
        u1.setUsername("User123");
        assertEquals(u1.getUsername(), "User123");
    }

    @Test
    void getUserType() {
        assertEquals(u1.getUserType(), "user");
    }

    @Test
    void setUserType() {
        u1.setUserType("admin");
        assertEquals(u1.getUserType(), "admin");
    }

    @Test
    void getEmail() {
        assertEquals(u1.getEmail(), "user1234@iastate.edu");
    }

    @Test
    void setEmail() {
        u1.setEmail("user1@iastate.edu");
        assertEquals(u1.getEmail(), "user1@iastate.edu");
    }

    @Test
    void getPassword() {
        assertEquals(u1.getPassword(), "U$er123");
    }

    @Test
    void setPassword() {
        u1.setPassword("User$123");
        assertEquals(u1.getPassword(), "User$123");
    }

    @Test
    void getFavMID() {
        assertEquals(u1.getFavMID(), 2);
    }

    @Test
    void setFavMID() {
        u1.setFavMID(4);
        assertEquals(u1.getFavMID(), 4);
    }

    @Test
    void getPfp() {
        assertEquals(u1.getPfp(), "img/userimg");
    }

    @Test
    void setPfp() {
        u1.setPfp("img/imgu");
        assertEquals(u1.getPfp(), "img/imgu");
    }
}