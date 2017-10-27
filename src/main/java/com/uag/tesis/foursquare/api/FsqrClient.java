package com.uag.tesis.foursquare.api;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompleteVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class FsqrClient {
	
	private static final String REDIRECT_URL = "http://www.foursquaretest.com";
	
	private static final String CLIENT_ID = "1BDIETZ4ZRY4VT2QKXDI3WZ4ZFLS1T2FQQ4GPVY3QPT102BA";
	
	private static final String CLIENT_SECRET = "L2QL1QHRMHOUJTS4JVHQAWSMIXLHIN04JK1QYJWMV0Z40VVI";
	
	private FoursquareApi foursquareApi;
	
	public FsqrClient(){
		foursquareApi = new FoursquareApi(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
	}
	
	public Result<VenuesSearchResult> venuesSearch(String ll) throws FoursquareApiException{
		return foursquareApi.venuesSearch(ll, null, null, null, null, 50, null, null, null,
				null, null, 350, null);
	}
	
	public CompleteVenue venue(String id) throws FoursquareApiException{
		return foursquareApi.venue(id).getResult();
	}
	
	public Result<Category[]> venuesCategories() throws FoursquareApiException{
		return foursquareApi.venuesCategories();
	}
}
