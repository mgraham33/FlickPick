package onetoone.Messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagesTest {

    Messages m1 = new Messages(11, 12, 22, "hello");
    Messages m2 = new Messages();

    @Test
    void getId() {
        assertEquals(m1.getId(), 0);
    }

    @Test
    void setId() {
        m1.setId(81);
        assertEquals(m1.getId(), 81);
    }

    @Test
    void getSenderid() {
        assertEquals(m1.getSenderid(), 11);
    }

    @Test
    void setSenderid() {
        m1.setSenderid(44);
        assertEquals(m1.getSenderid(), 44);
    }

    @Test
    void getReceiverid() {
        assertEquals(m1.getReceiverid(), 12);
    }

    @Test
    void setReceiverid() {
        m1.setReceiverid(33);
        assertEquals(m1.getReceiverid(), 33);
    }

    @Test
    void getFriendshipid() {
        assertEquals(m1.getFriendshipid(), 22);
    }

    @Test
    void setFriendshipid() {
        m1.setFriendshipid(88);
        assertEquals(m1.getFriendshipid(), 88);
    }

    @Test
    void getMessage() {
        assertEquals(m2.getMessage(), null);
    }

    @Test
    void setMessage() {
        m2.setMessage("Hello there");
        assertEquals(m2.getMessage(), "Hello there");
    }
}