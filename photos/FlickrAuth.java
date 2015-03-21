package blackco.photos;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;


public class FlickrAuth {

	  private static OAuthService service;
	
	  private static Token accessToken;
	  private static String userId;
	  
	  public static final String SERVICE = "blackco.photos.FlickrAuth";

	  /*
	  public static void main(String[] args) throws Exception {

			getAccessToken();  
			
	}
	*/

	public FlickrAuth(String apiKey, String apiSecret, String userId, 
			String accessKey, String accessSecret){
		
		accessToken = new Token( accessKey, accessSecret);
		service =  new ServiceBuilder().provider(FlickrApi.class).apiKey(apiKey).apiSecret(apiSecret).build();
		this.userId = userId;
	}
	
	public String getApiKey(){
		return accessToken.getToken();
	}
	
	public String getUserId(){
		return userId;
	}
	  
	public static Response get(OAuthRequest request){
	
		 	service.signRequest(accessToken, request);
		    return request.send();
		   
	}
	
	/*
	private static void getAccessToken() {

		if (accessToken == null) {
			Scanner in = new Scanner(System.in);

			System.out.println("=== Flickr's OAuth Workflow ===");
			System.out.println();

			// Obtain the Request Token
			System.out.println("Fetching the Request Token...");
			Token requestToken = service.getRequestToken();
			System.out.println("Got the Request Token!");
			System.out.println("Request Token looks like " + requestToken);
			System.out.println();

			System.out.println("Now go and authorize Scribe here:");
			String authorizationUrl = service.getAuthorizationUrl(requestToken);
			System.out.println(authorizationUrl + "&perms=write");
			System.out.println("And paste the verifier here");
			System.out.print(">>");
			Verifier verifier = new Verifier(in.nextLine());
			System.out.println();

			// Trade the Request Token and Verfier for the Access Token
			System.out
					.println("Trading the Request Token for an Access Token...");
			accessToken = service.getAccessToken(requestToken, verifier);
			System.out.println("Got the Access Token!");
			System.out.println("(if your curious it looks like this: "
					+ accessToken + " )");
			System.out
					.println("(you can get the username, full name, and nsid by parsing the rawResponse: "
							+ accessToken.getRawResponse() + ")");
			System.out.println();

			// Now let's go and ask for a protected resource!
			System.out
					.println("Now we're going to access a protected resource...");
		}
	}
*/

	
	
}
