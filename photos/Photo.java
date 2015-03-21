package blackco.photos;

public class Photo {
	
	public String id;
	public String owner;
	public String secret;
	public String server;
	public int farm;
	public String title;
	public int ispublic;
	public int isfamily;
	
	public String toString(){
		return "id=" + id +" ,title=" + title;
	}

}
