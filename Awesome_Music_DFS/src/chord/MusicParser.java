package chord;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import models.Record;
import models.Release;
import models.SongItem;

public class MusicParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Record.class, new SongDeserializer());
		Gson customGson = gsonBuilder.create();
		try (RemoteInputFileStream inputStream = new RemoteInputFileStream("src/music.json", false)) {

			// "Connecting" to the file that we need to read
			inputStream.connect();
			// Setting up the JsonReader to easily parse the items in it.
			Reader inputStreamReader = new InputStreamReader(inputStream);
			JsonReader jsonReader = new JsonReader(inputStreamReader);
			Record[] allOfMusic = customGson.fromJson(jsonReader, Record[].class);

			int perFile = allOfMusic.length / 10;

			for (int i = 0; i < 10; i++) {
				writeFile(allOfMusic, i * perFile, i * perFile + perFile, i);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	public static void writeFile(Record[] records, int start, int stop, int fileNumber) {
		// Setup the writer
		try {
			FileOutputStream outputStream = new FileOutputStream("file" + fileNumber + ".json");
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
			Writer buffWriter = new BufferedWriter(outputStreamWriter);
			Record[] thisFilesRecords = Arrays.copyOfRange(records, start, stop);
			SongItem[] thisFilesSongItems = new SongItem[stop - start];

			for (int i = 0; i < thisFilesRecords.length; i++) {
				thisFilesSongItems[i] = thisFilesRecords[i].getSongItem();				
			}
			System.out.println("File " + fileNumber + " contains " + thisFilesSongItems.length + " SongItems.");
			buffWriter.write(new Gson().toJson(thisFilesSongItems));
			buffWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
