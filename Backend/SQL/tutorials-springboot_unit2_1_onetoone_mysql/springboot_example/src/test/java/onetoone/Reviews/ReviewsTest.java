package onetoone.Reviews;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewsTest {

    Reviews r1 = new Reviews(1, 12, 3.5, "Good movie");
    Reviews r2 = new Reviews();
    @Test
    void getId() {
        assertEquals(r1.getId(), 0);
    }

    @Test
    void setId() {
        r1.setId(100);
        assertEquals(r1.getId(), 100);
    }

    @Test
    void getRating() {
        assertEquals(r1.getRating(), 3.5);
    }

    @Test
    void setRating() {
        r1.setRating(4.7);
        assertEquals(r1.getRating(), 4.7);
    }

    @Test
    void getMovieid() {
        assertEquals(r1.getMovieid(), 1);
    }

    @Test
    void setMovieid() {
        r1.setMovieid(17);
        assertEquals(r1.getMovieid(), 17);
    }

    @Test
    void getUserid() {
        assertEquals(r1.getUserid(), 12);
    }

    @Test
    void setUserid() {
        r1.setUserid(11);
        assertEquals(r1.getUserid(), 11);
    }

    @Test
    void getReason() {
        assertEquals(r1.getReason(), "Good movie");
    }

    @Test
    void setReason() {
        r1.setReason("One of the best movies i've ever seen!");
        assertEquals(r1.getReason(), "One of the best movies i've ever seen!");
    }
}