package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import chord.DFS;
import chord.DFS.FileJson;
import chord.DFS.FilesJson;
import chord.DFS.PagesJson;
import chord.RemoteInputFileStream;
import models.Record;
import models.SongItem;

/**
 * SongLibraryServices Class
 * 
 * @resource Received help from Lexzander Saplan and Stack Overflow
 */

public class SongLibraryServices {

	// private static SongLibrary mSongLibrary;

	public SongLibraryServices() {
		// mSongLibrary = SongLibrary.getInstance();
	}

	/**
	 * Searches the DFS for songs where the artist name or song title or album title
	 * contains the string searched for
	 * 
	 * @param search
	 *            parameter, page number of results
	 * @return a page of the search result
	 */
	public String searchForSong(String searchParam, String pageNumber) {
		if (searchParam.equals("")) {
			return getSixSongs(Integer.parseInt(pageNumber));
		} else {
			// Discovered ConcurrentLinkedQueue from this StackOverflow question
			// https://stackoverflow.com/questions/40325724/sharing-an-arraylist-between-two-threads
			ConcurrentLinkedQueue<SongItem> searchResults = new ConcurrentLinkedQueue();
			for (int i = 0; i < 10; i++) {
				// For each page in the DFS
				try {
					// Get the metadata
					FilesJson meta = DFSRepo.getInstance().getDFS().readMetaData();
					// Read the metadata into in a json string
					RemoteInputFileStream input = DFSRepo.getInstance().getDFS().read("chordMusic", i);
					input.connect();
					InputStreamReader is = new InputStreamReader(input);
					String resultJson = new BufferedReader(is).lines().collect(Collectors.joining("\n"));
					// For each page create a list of song items of that page
					ArrayList<SongItem> listOfSongItems = new Gson().fromJson(resultJson,
							new TypeToken<ArrayList<SongItem>>() {
							}.getType());
					// Pass the page's songs and the ConcurrentLinkedQueque to the thread and run it
					SearchThread searchThread = new SearchThread(listOfSongItems, searchParam, searchResults,
							Integer.parseInt(pageNumber));
					Thread thread = new Thread(searchThread);
					thread.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// With the results that are returned from the searches select the page of data
			// we need and return it
			int num = Integer.parseInt(pageNumber);
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 0; i < ((num - 1) * 6) + 6; i++) {
				if (i >= ((num - 1) * 6)) {
					if (searchResults.peek() != null) {
						sb.append(new Gson().toJson(searchResults.poll()));
					} else {
						sb.append(new Gson().toJson(new SongItem("", "", "", "")));
					}
				} else {
					searchResults.poll();
				}
			}
			sb.append("]");

			System.out.println("RESPONSE TO SEND: " + sb.toString());
			return sb.toString();

		}
	}

	// This method just pulls the first six songs off the current node
	public String getSixSongs(int pageNumber) {
		ArrayList<SongItem> results = new ArrayList();
		try {
			DFS dfs = DFSRepo.getInstance().getDFS();

			ConcurrentLinkedQueue<SongItem> result = new ConcurrentLinkedQueue<>();

			FilesJson meta = dfs.readMetaData();

			RemoteInputFileStream input = dfs.read("chordMusic", pageNumber);
			input.connect();

			System.out.println("Searching Page " + pageNumber);
			String json = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining("\n"));
			ArrayList<SongItem> songs = new Gson().fromJson(json, new TypeToken<ArrayList<SongItem>>() {
			}.getType());

			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 1 * (pageNumber - 1); i < 1 * (pageNumber - 1) + 6; i++) {
				// results.add(songs.get(i));
				sb.append(new Gson().toJson(songs.get(i)));
			}
			sb.append("]");
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Searches the SongLibrary for songs where the id matches the one searched for
	 * 
	 * @param search
	 *            parameter
	 * @return a JSON version of a SongItem as a string
	 */
	// public String searchForSongById(String searchParam) {
	// ArrayList<SongItem> items = new ArrayList<SongItem>();
	// StringBuilder sb = new StringBuilder();
	//
	// for (String recordId : mSongLibrary.getKeySet()) {
	// Record record = mSongLibrary.getRecord(recordId);
	// if (record.getSong().getId().equals(searchParam)) {
	// items.add(new SongItem(record.getSong().getTitle(),
	// record.getArtist().getName(),
	// record.getSong().getId(), record.getRelease().getName()));
	// }
	// }
	//
	// Gson gson = new Gson();
	// sb.append(gson.toJson(items.get(0)));
	//
	// return sb.toString();
	// }
}
