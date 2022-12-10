package onetoone.Links;

import onetoone.Links.Links;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestLinks {

    Links l = new Links("netflix.com", "hulu.com", "hbo.com", "disney.com", "amazon.com");
    Links l2 = new Links();

    @Test
    public void testGetId() {
        assertEquals(l.getId(), 0);
    }

    @Test
    public void testGetNetflix() {
        assertEquals(l.getNetflix(), "netflix.com");
    }

    @Test
    public void testGetHulu() {
        assertEquals(l.getHulu(), "hulu.com");
    }

    @Test
    public void testGetHbo() {
        assertEquals(l.getHboMax(), "hbo.com");
    }

    @Test
    public void testGetDisney() {
        assertEquals(l.getDisneyPlus(), "disney.com");
    }

    @Test
    public void testGetAmazon() {
        assertEquals(l.getAmazonPrime(), "amazon.com");
    }

    @Test
    public void testSetNetflix() {
        l.setNetflix("netflix.com/watch");
        assertEquals(l.getNetflix(), "netflix.com/watch");
    }

    @Test
    public void testSetHulu() {
        l.setHulu("hulu.com/watch");
        assertEquals(l.getHulu(), "hulu.com/watch");
    }

    @Test
    public void testSetHbo() {
        l.setHboMax("hbo.com/watch");
        assertEquals(l.getHboMax(), "hbo.com/watch");
    }

    @Test
    public void testSetDisney() {
        l.setDisneyPlus("disney.com/watch");
        assertEquals(l.getDisneyPlus(), "disney.com/watch");
    }

    @Test
    public void testSetAmazon() {
        l.setAmazonPrime("amazon.com/watch");
        assertEquals(l.getAmazonPrime(), "amazon.com/watch");
    }

    @Test
    public void testSetId() {
        l.setId(1);
        assertEquals(l.getId(), 1);
    }

}
