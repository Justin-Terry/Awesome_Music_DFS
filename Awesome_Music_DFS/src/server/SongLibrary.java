package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import chord.SongDeserializer;
import models.Record;

/**
 * Song Library Class
 * 
 * @author Justin
 *
 */
public class SongLibrary {

	private static final String JSON_PATH = "./src/resources/music.json";
	private static SongLibrary SONG_LIBRARY_SINGLE_INSTANCE;
	private static boolean isReady = false;

	private static Map<String, Record> songLibrary;

	/**
	 * Private constructor can only be called once inside the SongLibrary class.
	 * This ensures only one instance of songLibrary at a time.
	 * 
	 * @param args
	 *            unused
	 * @return N/A
	 */
	private SongLibrary() {
		createMap();
	}

	/**
	 * Returns the status of the SongLibrary
	 * 
	 * @param args
	 *            unused
	 * @return boolean
	 */
	public boolean isReady() {
		return isReady;
	}

	public void display() {
		int i = 0;
		if (this.isReady()) {
			for (Map.Entry<String, Record> entry : songLibrary.entrySet()) {
				if (i < 10) {
					System.out.println("Title: " + entry.getValue().getSong().getTitle() + " Artist: "
							+ entry.getValue().getArtist().getName());
					i++;
				}
			}
		}
	}

	/**
	 * Sorts the songLibrary Map by the song's title
	 * 
	 * @param args
	 *            unused
	 * @return void
	 */
	public static void sortByTitle() {
		isReady = false;
		Map<String, Record> treeMap = new TreeMap<String, Record>(new Comparator<String>() {
			@Override
			public int compare(String p1, String p2) {
				SongLibrary sl = SongLibrary.getInstance();
				Record rec1 = sl.getRecord(p1);
				Record rec2 = sl.getRecord(p2);
				return rec1.getSong().getTitle().compareTo(rec2.getSong().getTitle());
			};
		});
		for (Map.Entry<String, Record> entry : songLibrary.entrySet()) {
			treeMap.put(entry.getKey(), entry.getValue());
		}
		songLibrary = treeMap;
		isReady = true;
	}

	/**
	 * Sorts the songLibrary Map by the artist's name
	 * 
	 * @param args
	 *            unused
	 * @return void
	 */
	public static void sortByArtist() {
		isReady = false;
		Map<String, Record> treeMap = new TreeMap<String, Record>(new Comparator<String>() {
			@Override
			public int compare(String p1, String p2) {
				SongLibrary sl = SongLibrary.getInstance();
				Record rec1 = sl.getRecord(p1);
				Record rec2 = sl.getRecord(p2);
				return rec1.getArtist().getName().compareTo(rec2.getArtist().getName());
			};
		});
		for (Map.Entry<String, Record> entry : songLibrary.entrySet()) {
			treeMap.put(entry.getKey(), entry.getValue());
		}
		songLibrary = treeMap;
		isReady = true;

	}

	/**
	 * Returns the only active instance of SongLibrary
	 * 
	 * @param args
	 *            unused
	 * @return SongLibrary instance
	 */
	public static SongLibrary getInstance() {
		if (SONG_LIBRARY_SINGLE_INSTANCE == null) {
			SONG_LIBRARY_SINGLE_INSTANCE = new SongLibrary();
		}
		return SONG_LIBRARY_SINGLE_INSTANCE;
	}

	/**
	 * Returns the Record object of a queried song or null if song does not exist.
	 * 
	 * @param String
	 *            songId
	 * @return Record or null
	 */
	public Record getRecord(String songId) {
		if (songLibrary.containsKey(songId)) {
			return songLibrary.get(songId);
		} else {
			return null;
		}
	}

	/**
	 * Returns the size of our song library.
	 * 
	 * @param args
	 *            Unused
	 * @return int size of songLibrary
	 */
	public int size() {
		return songLibrary.size();
	}

	/**
	 * Searches through library for all Records that meet search query Returns
	 * ArrayList of songIDs
	 * 
	 * @param searchQuery
	 * @return
	 */
	public ArrayList<String> getSongID(String searchQuery) {
		ArrayList<String> matches = new ArrayList<String>();
		Iterator<Map.Entry<String, Record>> it = songLibrary.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Record> pair = (Map.Entry<String, Record>) it.next();
			Record r = (Record) pair.getValue();
			if (r.getArtist().getName().toLowerCase().contains(searchQuery.toLowerCase())
					|| r.getSong().getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
				matches.add((String) pair.getKey());
			}
		}

		return matches;
	}

	/**
	 * Gets an array of records and maps that array to the songLibrary HashMap.
	 * 
	 * @param args
	 *            Unused
	 * @return none
	 */
	private void createMap() {

		songLibrary = new HashMap<String, Record>();

		Record[] records = getJson();

		for (Record r : records) {
			songLibrary.put(r.getSong().getId(), r);
		}

		isReady = true;
	}
	
	public Set<String> getKeySet() {
		return songLibrary.keySet();
	}

	/**
	 * Creates an array of Records from the file provided by the professor.
	 * 
	 * @param args
	 *            Unused
	 * @return Record[] or null
	 * @exception IOException
	 *                or FileNotFoundException
	 * @see IOException or FileNotFoundException
	 */
	private Record[] getJson() {

		File file = new File(JSON_PATH);

		try {
			// Get the json from the file as a string
			String jsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

			// Create a builder and apply the custom deserializer to it.
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Record.class, new SongDeserializer());
			Gson customGson = gsonBuilder.create();

			// Convert the json array from the file into an array of Records and return it.
			Record[] records = customGson.fromJson(jsonString, Record[].class);
			return records;

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException at SongLibrary.getJson()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException at SongLibrary.getJson()");
			e.printStackTrace();
		}
		return null;
	}

}
