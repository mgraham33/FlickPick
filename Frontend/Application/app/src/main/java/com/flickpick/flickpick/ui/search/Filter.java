package com.flickpick.flickpick.ui.search;

import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Settings;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

/**
 * Class for containing all search filter information.
 *
 * @author Dustin Cornelison
 */
public class Filter {
	private String[] genreNames;
	private String[] serviceNames;
	
	private double lowRating;
	private double highRating;
	private int lowYear;
	private int highYear;
	private int lowMin;
	private int highMin;
	private boolean[] services;
	private boolean[] genres;
	
	/**
	 * Constructs a filter with default values:
	 * <pre>
	 * Low Rating: 0
	 * High Rating: 5
	 * Low Year: 0
	 * High Year: infinity
	 * Low Duration: 0
	 * High Duration: infinity
	 * Services: all true
	 * Genres: all true
	 * </pre>
	 *
	 * @param servFalse whether to default the services to false
	 */
	public Filter(boolean servFalse) {
		lowRating = 0;
		highRating = 5;
		lowYear = 0;
		highYear = -1;
		lowMin = 0;
		highMin = -1;
		serviceNames = AppController.getInstance().getServices();
		services = new boolean[serviceNames.length];
		if (servFalse) Arrays.fill(services, false);
		else for (int i = 0; i < services.length; i++) {
			//TODO: do this dynamically
			String title = serviceNames[i];
			if (title.toLowerCase().contains("netflix")) {
				services[i] = Settings.NETFLIX.get();
			} else if (title.toLowerCase().contains("hbo")) {
				services[i] = Settings.HBO.get();
			} else if (title.toLowerCase().contains("hulu")) {
				services[i] = Settings.HULU.get();
			} else if (title.toLowerCase().contains("prime")) {
				services[i] = Settings.PRIME.get();
			} else if (title.toLowerCase().contains("disney")) {
				services[i] = Settings.DISNEY.get();
			}
		}
		genreNames = AppController.getInstance().getGenres();
		genres = new boolean[genreNames.length];
		Arrays.fill(genres, false);
	}
	
	public Filter() {
		this(false);
	}
	
	
	/**
	 * @param lowRating what to set the low rating to
	 */
	public void setLowRating(double lowRating) {
		this.lowRating = ratingCheck(lowRating);
	}
	
	/**
	 * @param highRating what to set the high rating to
	 */
	public void setHighRating(double highRating) {
		this.highRating = ratingCheck(highRating);
	}
	
	/**
	 * @param lowYear what to set the low year to
	 */
	public void setLowYear(int lowYear) {
		this.lowYear = lowYear;
	}
	
	/**
	 * @param highYear what to set the high year to
	 */
	public void setHighYear(int highYear) {
		this.highYear = highYear;
	}
	
	/**
	 * @param lowMin what to set the low duration to
	 */
	public void setLowMin(int lowMin) {
		this.lowMin = lowMin;
	}
	
	/**
	 * @param highMin what to set the high duration to
	 */
	public void setHighMin(int highMin) {
		this.highMin = highMin;
	}
	
	/**
	 * Sets a specific service (at {@code index}) to the given {@code value}
	 * @param index index of the service in the service list given by the {@code AppController}
	 * @param value whether the filter for this service is active
	 *
	 * @see AppController#getServices()
	 */
	public void setService(int index, boolean value) {
		if (index >= 0) services[index] = value;
	}
	
	/**
	 * Sets a specific genre (at {@code index}) to the given {@code value}
	 * @param index index of the genre in the genre list given by the {@code AppController}
	 * @param value whether the filter for this genre is active
	 *
	 * @see AppController#getGenres()
	 */
	public void setGenre(int index, boolean value) {
		if (index >= 0) genres[index] = value;
	}
	
	/**
	 * For ease of use in requests; adds the filters to a parameter map.
	 * @param map the map to add to
	 */
	public void addToMap(@NotNull Map<String, String> map) {
		if (lowRating > 0) map.put("lowRating", String.valueOf(lowRating));
		if (highRating < 5) map.put("highRating", String.valueOf(highRating));
		if (lowYear > 0) map.put("lowYear", String.valueOf(lowYear));
		if (highYear >= 0) map.put("highYear", String.valueOf(highYear));
		if (lowMin > 0) map.put("lowMinutes", String.valueOf(lowMin));
		if (highMin >= 0) map.put("highMinutes", String.valueOf(highMin));
		
		for (int i = 0; i < services.length; i++) {
			String service = serviceNames[i];
			if (services[i]) map.put(service.toLowerCase(), "true");
		}
		
		for (int i = 0; i < genres.length; i++) {
			String genre = genreNames[i];
			if (genres[i]) map.put(genre.toLowerCase(), "true");
		}
	}
	
	/**
	 * Constrains ratings to 0-5, inclusive
	 * @param rating rating to constrain
	 * @return constrained rating
	 */
	private double ratingCheck(double rating) {
		return Math.min(5, Math.max(rating, 0));
	}
}
