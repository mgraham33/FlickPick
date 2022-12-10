package onetoone.Friends;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendsTest {

    Friends f1 = new Friends(11, 12, "accepted");
    Friends f2 = new Friends();
    @Test
    void getId() {
        assertEquals(f1.getId(), 0);
    }

    @Test
    void setId() {
        f1.setId(2);
        assertEquals(f1.getId(), 2);
    }

    @Test
    void getUser1() {
        assertEquals(f1.getUser1(), 11);
    }

    @Test
    void setUser1() {
        f1.setUser1(100);
        assertEquals(f1.getUser1(), 100);
    }

    @Test
    void getUser2() {
        assertEquals(f1.getUser2(), 12);
    }

    @Test
    void setUser2() {
        f1.setUser2(200);
        assertEquals(f1.getUser2(), 200);
    }

    @Test
    void getFriendtype() {
        assertEquals(f1.getFriendtype(), "accepted");
    }

    @Test
    void setFriend_type() {
        f1.setFriend_type("denied");
        assertEquals(f1.getFriendtype(), "denied");
    }
}