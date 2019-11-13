package models;

import java.util.ArrayList;
import java.util.Map;

/**
 *	The Playlist class holds a list of String objects
 */
public class Playlist {
	@Override
	public String toString() {
		return "Playlist [user_id=" + user_id + ", playlistName=" + playlistName + ", playlist=" + playlist + "]";
	}

	int user_id;
	ArrayList<String> playlist;
	String playlistName;
	


	public Playlist() {
		
	}
	
public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setPlaylist(ArrayList<String> playlist) {
		this.playlist = playlist;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

/**
 *	Constructor. Creates a new Playlist object
 * 
 * @param name	The name of the Playlist
 */
	public Playlist(String name, int id) {
		user_id = id;
		playlist = new ArrayList<>();
		playlistName = name;
	}
	
	public Playlist(int id, String name, String[] playlist) {
		user_id = id;
		this.playlist = new ArrayList<>();
		playlistName = name;
		for(int i = 0; i < playlist.length; i++) {
			this.playlist.add(playlist[i]);
		}
	}
	
/**
 * Adds a String r to the Playlist
 * @param r	A Record object
 */
	public void addPlaylist(Record r) {
		playlist.add(r.getSong().getId());
	}

	/**
	 * Adds a String r to the Playlist
	 * @param r	A String object
	 */
		public void addPlaylist(String r) {
			playlist.add(r);
		}
	
	/**
	 * 	Removes a String from the playlist using String n
	 * @param n A String n is the name of the song
	 */
	public void removeStringByName(String n) {
		for(int i = 0; i < playlist.size(); i++) {
			if(playlist.get(i).equals(n)) {
				playlist.remove(i);
			}
		}
	}
/**
 * 	Removes a String r from the playlist
 * @param r A String r
 */
	public void removeStringByRecord(Record r) {
		for(int i = 0; i < playlist.size(); i++) {
			if(playlist.get(i).equals(r.getSong().getId())) {
				playlist.remove(i);
			}
		}
	}
	
/**
 * 	Changes the name of the Playlist
 * @param n The name change by String n
 */
	public void changePlaylistName(String n) {
		playlistName = n;
	}

/**
 * Gets the playlist
 * @return playlist
 */
	public ArrayList<String> getPlaylist() {
		return playlist;
	}

	/**
	 * Gets the playlist name
	 * @return playlistName
	 */
	public String getPlaylistName() {
		return playlistName;
	}

	/**
	 * Gets the playlist name
	 * @return playlistName
	 */
		public int getUser_id() {
			return user_id;
		}

	public Record getRecord(String id,Map<String, Record> sl) {
		return sl.get(id);
	}
	

	public void noNullPlaylist() {
		for(int i = 0; i < playlist.size(); i++) {
			if(playlist.get(i) == null) {
				playlist.remove(i);
			}
		}
	}
	
}
