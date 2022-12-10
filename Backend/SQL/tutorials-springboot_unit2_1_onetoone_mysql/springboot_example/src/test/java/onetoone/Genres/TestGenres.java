package onetoone.Genres;

import onetoone.Genres.Genres;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestGenres {

    Genres g = new Genres("action", "horror", "comedy");
    Genres g2 = new Genres();

    @Test
    public void testGetId() {
        assertEquals(g.getId(), 0);
    }

    @Test
    public void testGetGenre1() {
        assertEquals(g.getGenre1(), "action");
    }

    @Test
    public void testGetGenre2() {
        assertEquals(g.getGenre2(), "horror");
    }

    @Test
    public void testGetGenre3() {
        assertEquals(g.getGenre3(), "comedy");
    }

    @Test
    public void testSetGenre1() {
        g.setGenre1("adventure");
        assertEquals(g.getGenre1(), "adventure");
    }

    @Test
    public void testSetGenre2() {
        g.setGenre2("sci-fi");
        assertEquals(g.getGenre2(), "sci-fi");
    }

    @Test
    public void testSetGenre3() {
        g.setGenre3("war");
        assertEquals(g.getGenre3(), "war");
    }

    @Test
    public void testSetId() {
        g.setId(1);
        assertEquals(g.getId(), 1);
    }

}
