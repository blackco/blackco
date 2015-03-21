package blackco.photos;

import java.util.ArrayList;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;


import blackco.photos.Services.Service;

public class GetPhotos {

	private static int pages;
	private static ArrayList<String> flickr;

	public ArrayList<String> getFlickrList(){
		
		flickr = new ArrayList<String>();

		PageSummary summary = getFlickrApiGetPhotos(1);

		for ( Photo s : summary.photos)
			flickr.add(s.title);


		for (int i = 2; i < summary.pages + 1; i++) {

			PageSummary nextPage = getFlickrApiGetPhotos(i);

			for ( Photo s : nextPage.photos)
				flickr.add(s.title);


		}

		return flickr;
		
		
	}


	private static PageSummary getFlickrApiGetPhotos(int pageNumber) {

		FlickrAuth auth = (FlickrAuth) Services.getService(Service.FLICKR_AUTH);

		OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api.flickr.com/services/rest/");
		request.addQuerystringParameter("method", "flickr.people.getPhotos");

		request.addQuerystringParameter("api_key", auth.getApiKey());
		request.addQuerystringParameter("user_id", auth.getUserId());
		request.addQuerystringParameter("per_page", "100");
		request.addQuerystringParameter("page", Integer.toString(pageNumber));
		request.addQuerystringParameter("format", "json");
		request.addQuerystringParameter("nojsoncallback", "1");

		Response response = auth.get(request);

		StringReader reader = new StringReader(response.getBody());

		JsonReader jsonReader = Json.createReader(reader);

		return buildPageSummary(jsonReader);

	}

	
	
	
	
	private static PageSummary buildPageSummary(JsonReader jsonReader){
		
		PageSummary s = new PageSummary();
		
		
		JsonObject jobj = jsonReader.readObject();


		JsonObject object = (JsonObject) jobj.get("photos");
		//JsonObject object = (JsonObject) value;
		
		s.page = object.getInt("page");
		s.pages = object.getInt("pages");
		s.perPage = object.getInt("perpage");
		s.total = object.getString("total");
		
		//System.out.println("BUILD PAGE SUMMARY ===== " + s );
		
		
		JsonArray array = (JsonArray) object.get("photo");
        for (JsonValue val : array){
        	
        	Photo p = new Photo();
        	JsonObject obj = (JsonObject) val;
        	
        	p.id = obj.getString("id");
        	p.owner = obj.getString("owner");
        	p.secret = obj.getString("secret");
        	p.farm = obj.getInt("farm");
        	p.title = obj.getString("title");
        	p.ispublic = obj.getInt("ispublic");
        	p.isfamily = obj.getInt("isfamily");
        	
        	s.photos.add(p);
        }
           
		
		return s;
	}
	

	
}
