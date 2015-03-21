package blackco.photos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import blackco.photos.Services.Service;
import Photos.PhotoFinder;


public class Comparison {

	private static ArrayList<String> flickr = new ArrayList<String>();

	private static FlickrAuth flickrAuth;
	
	private HashMap<String, Path> ondisk = new HashMap<String, Path>();

	
	private static String onDiskPath; 
	private static String onDiskFileList;
	private static String onFlickrList; 
	private static String results; 
	
	private static int pages;
	

	public static void main(String[] args) {

		String requestKey = null;
		String requestSecret = null;
		String userId = null;
		String accessKey = null;
		String accessSecret = null;
		
		int i;

		for (i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-path":
				if (i < args.length)
					onDiskPath = args[++i];
				break;

			case "-reqKey":
				if (i < args.length)
					requestKey = args[++i];
				break;

			case "-reqSecret":
				if (i < args.length)
					requestSecret = args[++i];
				break;

			case "-accessKey":
				if ( i< args.length)
					accessKey = args[++i];
				break;
			
			case "-accessSecret":
				if ( i< args.length)
					accessSecret = args[++i];
				break;
				
			case "-userId":
				if (i < args.length)
					userId = args[++i];
				break;
				
			case "-debugOnDiskList":
				if (i < args.length)
					onDiskFileList = args[++i];
				break;

			case "-debugOnFlickrList":
				if (i < args.length)
					onFlickrList = args[++i];
				break;

			case "-results":
				if (i < args.length)
					results = args[++i];
				break;
			}
		}
		
		if ( requestSecret != null &&  requestKey != null && userId != null  
				&& accessKey != null & accessSecret != null){
		
			 Services.addService(Service.FLICKR_AUTH, new FlickrAuth(requestKey, requestSecret
					 , userId, accessKey, accessSecret));	
		} else {
			throw new RuntimeException("Flickr Authentication information is mandatory");
		}
			
		

		Comparison c = new Comparison();

		try {
			c.compare();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void compare() throws Exception {

		FileWriter f1;

		GetPhotos g = new GetPhotos();
		
		flickr = g.getFlickrList();

		if (onFlickrList != null) {
			f1 = new FileWriter(onFlickrList);

			for (String p : flickr) {
				f1.write(p);
				f1.write(System.getProperty("line.separator"));
			}

			f1.close();
		}

		getOnDiskList();

		if (onDiskFileList != null) {
			f1 = new FileWriter(onDiskFileList);

			for (Path p : ondisk.values()) {
				f1.write(p.toFile().getAbsolutePath());
				f1.write(System.getProperty("line.separator"));
			}

			f1.close();
		}

		for (String s : flickr) {
			ondisk.remove(s);
		}

		if (results != null) {
			f1 = new FileWriter(results);

			for (Path p : ondisk.values()) {
				f1.write(p.toFile().getAbsolutePath());
				f1.write(System.getProperty("line.separator"));
			}

			f1.close();
		}
	}



	private void getOnDiskList() {

		ArrayList<PhotoFinder> patterns = new ArrayList();

		patterns.add(new PhotoFinder("*.JPG"));
		patterns.add(new PhotoFinder("*.jpg"));
		patterns.add(new PhotoFinder("*.AVI"));
		patterns.add(new PhotoFinder("*.avi"));

		for (PhotoFinder finder : patterns) {

			try {
				Files.walkFileTree(Paths.get(onDiskPath),
						EnumSet.of(FileVisitOption.FOLLOW_LINKS),
						Integer.MAX_VALUE, finder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Path s : finder.getFound()) {
				ondisk.put(stripExtension(s.getFileName().toString()), s);
				// System.out.println(s);
			}

		}


	}


	private static String stripExtension(String str) {
		// Handle null case specially.

		if (str == null)
			return null;

		// Get position of last '.'.

		int pos = str.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.

		if (pos == -1)
			return str;

		// Otherwise return the string, up to the dot.

		return str.substring(0, pos);
	}
	
	
}
