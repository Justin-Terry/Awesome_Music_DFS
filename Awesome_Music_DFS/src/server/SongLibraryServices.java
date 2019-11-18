package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
 * @author Justin
 *
 */

public class SongLibraryServices {

	private static SongLibrary mSongLibrary;

	public SongLibraryServices() {
		mSongLibrary = SongLibrary.getInstance();
	}

	/**
	 * Searches the SongLibrary for songs where the artist name or song title
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
			ArrayList<SongItem> items = new ArrayList<SongItem>();
			StringBuilder sb = new StringBuilder();
			sb.append("[");

			for (String recordId : mSongLibrary.getKeySet()) {
				Record record = mSongLibrary.getRecord(recordId);
				if (record.getArtist().getName().contains(searchParam)
						|| record.getSong().getTitle().contains(searchParam)) {
					items.add(new SongItem(record.getSong().getTitle(), record.getArtist().getName(),
							record.getSong().getId(), record.getRelease().getName()));
				}
			}

			int start = (Integer.parseInt(pageNumber) - 1) * 6;
			for (int i = start; i < start + 6; i++) {
				if (items.size() > i) {
					Gson gson = new Gson();
					sb.append(gson.toJson(items.get(i)));
				}
			}

			sb.append("]");
			return sb.toString();
		}
	}

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
			for(int i = 1 * (pageNumber - 1); i < 1 * (pageNumber - 1) + 6; i++) {
//				results.add(songs.get(i));
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
	public String searchForSongById(String searchParam) {
		ArrayList<SongItem> items = new ArrayList<SongItem>();
		StringBuilder sb = new StringBuilder();

		for (String recordId : mSongLibrary.getKeySet()) {
			Record record = mSongLibrary.getRecord(recordId);
			if (record.getSong().getId().equals(searchParam)) {
				items.add(new SongItem(record.getSong().getTitle(), record.getArtist().getName(),
						record.getSong().getId(), record.getRelease().getName()));
			}
		}

		Gson gson = new Gson();
		sb.append(gson.toJson(items.get(0)));

		return sb.toString();
	}
}
