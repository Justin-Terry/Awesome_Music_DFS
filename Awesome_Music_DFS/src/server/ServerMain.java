package server;

import java.util.Scanner;

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
		
		System.out.print("What port for server? >> ");
		Scanner input = new Scanner(System.in);
		int port = input.nextInt();
		System.out.print("What port should the chord listen on? >> ");
		int chordPort = input.nextInt();
		System.out.print("What port should the chord join? >> ");
		int portToJoin = input.nextInt();
		// Start the server
		new Server(port, chordPort, portToJoin).start();
	}

};
