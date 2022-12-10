package com.flickpick.flickpick.util;

import junit.framework.TestCase;

import org.junit.Test;

public class AppControllerTest extends TestCase {

    @Test
    public void testGetMovieTitleId() {
        String title = "This is a Test Movie Title";
        int id = 23;
        String output;
        String expected = "This is a Test Movie Title";

        AppController ac = new AppController();
        output = ac.getMovieTitleId(title, id);

        assertEquals(expected, output);
    }

    @Test
    public void testCapitalCase() {
        String title = "genre";
        String output;
        String expected = "Genre";

        AppController ac = new AppController();
        output = ac.capitalCase(title);

        assertEquals(expected, output);
    }

    @Test
    public void testCapitalCase2() {
        String title = "genre movie";
        String output;
        String expected = "Genre Movie";

        AppController ac = new AppController();
        output = ac.capitalCase(title);

        assertEquals(expected, output);
    }

    @Test
    public void testHTTPEncode() {
        String url = "http://coms-309-017.class.las.iastate.edu:8080/genres/genre/science fiction";
        String output;
        String expected = "http%3A%2F%2Fcoms-309-017.class.las.iastate.edu%3A8080%2Fgenres%2Fgenre%2Fscience+fiction";

        AppController ac = new AppController();
        output = ac.HTTPEncode(url);

        assertEquals(expected, output);
    }

    @Test
    public void testHTTPEncode2() {
        String url = "http://coms-309-017.class.las.iastate.edu:8080/users/23";
        String output;
        String expected = "http%3A%2F%2Fcoms-309-017.class.las.iastate.edu%3A8080%2Fusers%2F23";

        AppController ac = new AppController();
        output = ac.HTTPEncode(url);

        assertEquals(expected, output);
    }
}