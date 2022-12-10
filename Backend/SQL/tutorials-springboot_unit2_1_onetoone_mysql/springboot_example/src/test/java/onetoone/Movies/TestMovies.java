package onetoone.Movies;

import onetoone.Movies.Movies;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestMovies {

    Movies m = new Movies("My Title", 120, 2002, 0.0, "great movie", "image.jpg");
    Movies m2 = new Movies();

    @Test
    public void testGetId() {
        assertEquals(m.getId(), 0);
    }

    @Test
    public void testGetTitle()  {
        assertEquals(m.getTitle(), "My Title");
    }

    @Test
    public void testGetDuration()  {
        assertEquals(m.getMinutes(), 120);
    }

    @Test
    public void testGetYear()  {
        assertEquals(m.getYear(), 2002);
    }

    @Test
    public void testGetRating()  {
        assertEquals(m.getRating(), 0.0, 0);
    }

    @Test
    public void testGetDescription()  {
        assertEquals(m.getDescription(), "great movie");
    }

    @Test
    public void testGetImage()  {
        assertEquals(m.getPicture(), "image.jpg");
    }

    @Test
    public void testSetId() {
        m.setId(1);
        assertEquals(m.getId(), 1);
    }

    @Test
    public void testSetTitle()  {
        m.setTitle("New Title");
        assertEquals(m.getTitle(), "New Title");
    }

    @Test
    public void testSetDuration()  {
        m.setMinutes(90);
        assertEquals(m.getMinutes(), 90);
    }

    @Test
    public void testSetYear()  {
        m.setYear(1999);
        assertEquals(m.getYear(), 1999);
    }

    @Test
    public void testSetRating()  {
        m.setRating(2.2);
        assertEquals(m.getRating(), 2.2, 0);
    }

    @Test
    public void testSetDescription()  {
        m.setDescription("best movie");
        assertEquals(m.getDescription(), "best movie");
    }

    @Test
    public void testSetImage()  {
        m.setPicture("Picture.jpg");
        assertEquals(m.getPicture(), "Picture.jpg");
    }

}