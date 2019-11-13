package server;

import java.util.ArrayList;

import com.google.gson.Gson;

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
		 * Searches the SongLibrary for songs where the artist name or song title contains the string searched for
		 * @param search parameter, page number of results
		 * @return a page of the search result
		 */
		public String searchForSong(String searchParam, String pageNumber) {
			ArrayList<SongItem> items = new ArrayList<SongItem>();
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			
			for(String recordId : mSongLibrary.getKeySet()) {
				Record record = mSongLibrary.getRecord(recordId);
				if(record.getArtist().getName().contains(searchParam) || record.getSong().getTitle().contains(searchParam)) {
					items.add(new SongItem(record.getSong().getTitle(), record.getArtist().getName(), record.getSong().getId(), record.getRelease().getName()));
				}			
			}
			
			int start = (Integer.parseInt(pageNumber) - 1) * 6;
			for(int i = start; i < start + 6; i++) {
				if(items.size() > i) {
					Gson gson = new Gson();
					sb.append(gson.toJson(items.get(i)));
				}
			}
			
			sb.append("]");
			return sb.toString();
		}
		
		/**
		 * Searches the SongLibrary for songs where the id matches the one searched for
		 * @param search parameter
		 * @return a JSON version of a SongItem as a string
		 */		
		public String searchForSongById(String searchParam) {
			ArrayList<SongItem> items = new ArrayList<SongItem>();
			StringBuilder sb = new StringBuilder();
			
			for(String recordId : mSongLibrary.getKeySet()) {
				Record record = mSongLibrary.getRecord(recordId);
				if(record.getSong().getId().equals(searchParam)) {
					items.add(new SongItem(record.getSong().getTitle(), record.getArtist().getName(), record.getSong().getId(), record.getRelease().getName()));
				}			
			}
			
			Gson gson = new Gson();
			sb.append(gson.toJson(items.get(0)));

			return sb.toString();
		}
}
