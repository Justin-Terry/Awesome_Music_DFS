package models;

public class SongItem {
	
	private String title;
	private String id;
	private String artist;
	private String record;
    
    public SongItem(String title, String artist, String id, String record) {
    	this.title = new String(title);	
		this.id = new String(id);
		this.artist = new String(artist);
		this.record = new String(record);
	}
    
    public String toJsonString() {
    	return "{" + "\"title\":\"" + title + "\", \"artist\":\"" + artist +"\",\"id\":\"" + id + "\", \"recordName\" : \"" + record + "\"}";
    }

	@Override
	public String toString() {
		return "Song [title=" + title + ", artist=" + artist +  ", id=" + id + "]";
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public String getArtist() {
		return artist;
	}

	public String getRecord() {
		return record;
	}


}
