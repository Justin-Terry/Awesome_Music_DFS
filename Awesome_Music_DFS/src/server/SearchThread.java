package server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import models.SongItem;

public class SearchThread implements Runnable{
	private ArrayList<SongItem> songItems;
	private String param;
	private ConcurrentLinkedQueue<SongItem> search;
	private int pageNumber;
	
	public SearchThread(ArrayList<SongItem> songItems, String param, ConcurrentLinkedQueue<SongItem> search, int pageNumber) {
		this.songItems = songItems;
		this.param = param;
		this.search = search;
		this.pageNumber = pageNumber;
	}
	
	@Override
	public void run() {
		System.out.println("SONGITEMS SIZE: " + songItems.size());
		Thread thisThread = Thread.currentThread();
		System.out.println("SeachThread with id of " + thisThread.getId() + " is running.");
		for(int i = 0; i < songItems.size(); i++) {
			if(search.size() > 6 * pageNumber - 1) {
				// Already a full page of results
				return;
			}
			if(songItems.get(i).getArtist().toLowerCase().contains(param.toLowerCase())) {
				if(!search.contains(songItems.get(i))) {
					search.add(songItems.get(i));
				}
			}
			if(songItems.get(i).getRecord().toLowerCase().contains(param.toLowerCase())) {
				if(!search.contains(songItems.get(i))) {
					search.add(songItems.get(i));
				}			}
			if(songItems.get(i).getTitle().toLowerCase().contains(param.toLowerCase())) {
				if(!search.contains(songItems.get(i))) {
					search.add(songItems.get(i));
				}			}
		}
	}

}
