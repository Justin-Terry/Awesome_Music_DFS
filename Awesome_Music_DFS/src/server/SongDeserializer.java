package server;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import models.Artist;
import models.Record;
import models.Release;
import models.Song;

class SongDeserializer implements JsonDeserializer<Record> {
	  
		@Override
	public Record deserialize(JsonElement json, Type Record, JsonDeserializationContext context) throws JsonParseException {
		// Convert string to JSON object
	    JsonObject jsonObject = json.getAsJsonObject();
	    
	    Gson gson = new Gson();
	    // Create a Song object from the json.
	    Song song = gson.fromJson(jsonObject.get("song"), Song.class);
	    // Create an Artist object from the json.
	    Artist artist = gson.fromJson(jsonObject.get("artist"), Artist.class);
	    // Create a Release object from the json.
	    Release release = gson.fromJson(jsonObject.get("release"), Release.class);
	    // Create a Record object from the other objects.
	    Record record = new Record(artist, release, song);
	    
	    return record;
	    
	}
}
