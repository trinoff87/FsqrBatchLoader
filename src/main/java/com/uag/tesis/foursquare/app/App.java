package com.uag.tesis.foursquare.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.uag.tesis.foursquare.api.FsqrClient;
import com.uag.tesis.foursquare.dao.FsqrDataDao;
import com.uag.tesis.fousquare.domain.FsqrData;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.CompleteVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import static com.uag.tesis.foursquare.util.Constants.*;

/**
 * Basic search example
 * 
 * @TODO - more examples please :)
 * @author rmangi
 *
 */
public class App {

	/*
	 * static { System.setProperty("http.proxyHost", "localhost");
	 * System.setProperty("http.proxyPort", "8888");
	 * System.setProperty("https.proxyHost", "localhost");
	 * System.setProperty("https.proxyPort", "8888");
	 * System.setProperty("proxySet", "true"); System.setProperty("proxyHost",
	 * "127.0.0.1"); System.setProperty("proxyPort", "8888");
	 * System.setProperty("javax.net.ssl.trustStore",
	 * "C:\\DevTools\\Fiddler\\FiddlerKeystore");
	 * System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
	 * 
	 * }
	 */
	
	private ArrayList<String> categories;
	
	private static List<FsqrData> dataList = new ArrayList<FsqrData>();
	
	private FsqrClient fsqrClient;
	private ApplicationContext context;
	
	public App(){
		fsqrClient = new FsqrClient();
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	public static void main(String[] args) {
		System.out.println("Foursquare api load started!");
		App be =  new App();
		FsqrDataDao fsqrDataDao = (FsqrDataDao) be.context.getBean("fsqrDataDao");
		long startTime = System.currentTimeMillis();
		try {

			be.searchCategories();
			
			for(int i=0; i< POINTS.length; i++){
				be.searchVenues(POINTS[i], NAMES[i]);
			}
	        
	        System.out.println("Storing data on mysql...");
	        for(FsqrData data: dataList){
	        	fsqrDataDao.insertFsqrData(data);
	        }
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Foursquare api load ended!");
			System.out.println("Process took " + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(totalTime),
					TimeUnit.MILLISECONDS.toSeconds(totalTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))) + " to finish!");

		} catch (FoursquareApiException e) {
			System.out.println(e.getStackTrace());
		}
	}

	private void searchCategories() throws FoursquareApiException {
		categories = new ArrayList<String>();

		Result<Category[]> cat = new Result<Category[]>(null, null);
		cat = fsqrClient.venuesCategories();

		if (cat.getMeta().getCode() == 200) {
			for (Category category : cat.getResult()) {
				if (category.getId().equals("4d4b7104d754a06370d81259") // Arts & Entertainment
						|| category.getId().equals("4d4b7105d754a06373d81259") // Events
						|| category.getId().equals("4d4b7105d754a06374d81259") // Food
						|| category.getId().equals("4d4b7105d754a06376d81259") // Nightlife Spot
						|| category.getId().equals("4d4b7105d754a06377d81259") // Outdoors & Recreation
						|| category.getId().equals("4bf58dd8d48988d1fd941735") // Shopping  Mall
						|| category.getId().equals("5744ccdfe4b0c0459246b4dc")) { // ShoppingPlaza

					categories.add(category.getId());

					for (Category child : category.getCategories()) {
						categories.add(child.getId());
					}
				}
			}
		}
	}

	public void searchVenues(String ll, String name) throws FoursquareApiException {
		int likes = 0;
		int checkins = 0;
		int users = 0;
		ArrayList<CompleteVenue> venues;
		venues = new ArrayList<CompleteVenue>();
		Result<VenuesSearchResult> result = fsqrClient.venuesSearch(ll);
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
		if (result.getMeta().getCode() == 200) {
			for (CompactVenue venue : result.getResult().getVenues()) {
				CompleteVenue theVenue = fsqrClient.venue(venue.getId());
				if (theVenue.getCategories() != null){
					if (theVenue.getCategories().length > 0){
						for (String current : categories) {
							if (current.matches(theVenue.getCategories()[0].getId())) {
								venues.add(theVenue);
							}
						}
					}
				}
			}
		} else {
			System.out.println("Error occured: ");
			System.out.println("  code: " + result.getMeta().getCode());
			System.out.println("  type: " + result.getMeta().getErrorType());
			System.out.println("  detail: " + result.getMeta().getErrorDetail());
		}
		
		for (CompleteVenue cv : venues) {
			users += cv.getStats().getUsersCount();
			checkins += cv.getStats().getCheckinsCount();
			likes += cv.getLikes().getCount();
		}
		
		FsqrData data = new FsqrData(format, name, users, checkins, likes);
		dataList.add(data);
	}
}
