package models;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Metadata {
	private String name;
	private int size;
	private int creationTS;
	private int readTS;
	private int numberOfPages;
	private int maxPageSize;
	private ArrayList<Page> pages;
	
	@Override
	public String toString() {
		return "\"file\":\"{ \"name\":\"" + name + "\", \"size\":\"" + size + "\", \"creationTS\":\"" + creationTS
				+ "\", \"readTS\":\"" + readTS + "\", \"numberOfPages\":\"" + numberOfPages + "\", \"maxPageSize\":\" " + maxPageSize
				+ "\", \"pages\":\"" + pages + "\"}";
	}


	public Metadata(String name, int size, int creationTS, int readTS, int numberOfPages, int maxPageSize,
			ArrayList<Page> pages) {
		this.name = name;
		this.size = size;
		this.creationTS = creationTS;
		this.readTS = readTS;
		this.numberOfPages = numberOfPages;
		this.maxPageSize = maxPageSize;
		this.pages = pages;
	}
	
}
