package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import models.Playlist;
import models.SongItem;

/**
 * PlaylistServices Class
 * 
 * @author Justin Terry
 * 
 */

public class PlaylistServices {
	private static final String PLAYLISTS_PATH = "./src/resources/playlists.json";
	private static ArrayList<Playlist> playlists;
	private static ArrayList<Playlist> allPlaylists;

	public PlaylistServices() {
		playlists = new ArrayList<Playlist>();
		allPlaylists = new ArrayList<Playlist>();
	}
	
	/**
	 * Returns all the playlists associated with a userId
	 * @param userId
	 * @return all the playlists associated with a userId
	 */
	public String getPlaylists(String userId) {
		try {
			loadPlaylists(userId);
			Gson gson = new Gson();
			System.out.println("PLAYLISTS: " + playlists);
			System.out.println("PLAYLISTS JSON: " + gson.toJson(playlists));
			return gson.toJson(playlists);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads all the playlists from JSON object
	 * @param userId
	 * @return all the playlists associated with a userId
	 */
	private void loadPlaylists(String userId) throws FileNotFoundException {
		Gson gson1 = new Gson();
		JsonReader pReader = new JsonReader(new FileReader(PLAYLISTS_PATH));
		Playlist[] pLists = gson1.fromJson(pReader, Playlist[].class);
		System.out.println("PLists: " + pLists);
		for (Playlist p : pLists) {
			if (userId.equals(String.valueOf(p.getUser_id()))) {
				playlists.add(p);
			}
		}
		
	}

	private void loadAllPlaylists() throws FileNotFoundException {
		Gson gson1 = new Gson();
		JsonReader pReader = new JsonReader(new FileReader(PLAYLISTS_PATH));
		Playlist[] pLists = gson1.fromJson(pReader, Playlist[].class);
		for (Playlist p : pLists) {
			allPlaylists.add(p);
		}
	}

	public String createPlaylist(String playlistname, String userId) {
		try {
			loadPlaylists(userId);
			loadAllPlaylists();
			for (Playlist p : playlists) {
				if (userId.equals(String.valueOf(p.getUser_id())) && (p.getPlaylistName().equals(playlistname)))
					return "Error: Title already used.";
			}

			Playlist newPlaylist = new Playlist(playlistname, Integer.parseInt(userId));
			System.out.println("New Playlist" + newPlaylist);
			Gson gson = new Gson();
			ArrayList<Playlist> newPlaylistJSON = new ArrayList<Playlist>();

			for (Playlist p : allPlaylists) {
				newPlaylistJSON.add(p);
			}
			newPlaylistJSON.add(newPlaylist);
			System.out.println(allPlaylists);
			FileWriter file = new FileWriter(PLAYLISTS_PATH);
			file.append(gson.toJson(newPlaylistJSON));
			file.close();
			return "Success";
		} catch (Exception e) {
			return "Error creating playlist";
		}
	}

	public String addSongToPlaylist(String userId, String playListName, String songId, String title, String artist, String record) throws IOException {
		try {
			loadAllPlaylists();
			
			SongItem t = new SongItem(title, artist, songId, record);
	

			for (Playlist p : allPlaylists) {
				if (p.getPlaylistName().equals(playListName) && userId.equals(String.valueOf(p.getUser_id()))) {
					p.addPlaylist(t);
					System.out.println(p.toString());
				}
			}
			Gson gson = new Gson();
			FileWriter file = new FileWriter(PLAYLISTS_PATH);
			// Replace contents of users.json with newUserJSON

			file.append(gson.toJson(allPlaylists));
			file.close();

			return "Success";
		} catch (Exception e) {
			return "Error adding song";
		}
	}

	public String removePlaylist(String playlistname, String userId) throws IOException {
		try {
			loadAllPlaylists();
			ArrayList<Playlist> newPlaylistJSON = new ArrayList<Playlist>();

			for (int i = 0; i < allPlaylists.size(); i++) {
				if (!allPlaylists.get(i).getPlaylistName().equals(playlistname) || !userId.equals(String.valueOf(allPlaylists.get(i).getUser_id()))) {
					newPlaylistJSON.add(allPlaylists.get(i));
				}
			}
			Gson gson = new Gson();
			FileWriter file = new FileWriter(PLAYLISTS_PATH);
			file.append(gson.toJson(newPlaylistJSON));
			file.close();
			return "Success";
		} catch (Exception e) {
			return null;
		}
	}
}
