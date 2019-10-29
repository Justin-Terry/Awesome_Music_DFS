package models;

import javafx.beans.property.SimpleStringProperty;

public class Record {

	private Artist artist;
	private Release release;
	private Song song;
	
	
	public Record(Artist artist, Release release, Song song) {
		super();
		this.artist = artist;
		this.release = release;
		this.song = song;
		
		}
	
	@Override
	public String toString() {
		return "Record [artist=" + artist + ", release=" + release + ", song=" + song + "]";
	}

	public Artist getArtist() {
		return artist;
	}

	public Release getRelease() {
		return release;
	}

	public Song getSong() {
		return song;
	}
	
}
