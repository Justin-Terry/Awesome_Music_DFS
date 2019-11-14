package server;

/**
 * Song Library Class
 * 
 * @author Justin
 *
 */

public class ServerMain {

	public static void main(String[] args) throws NumberFormatException, Exception {
		// Create the song library
		SongLibrary.getInstance();
		// Start the server
		new Server(Integer.parseInt(args[0])).start();

	}

};
