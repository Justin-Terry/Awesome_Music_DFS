package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import chord.DFS;
import chord.DFSCommand;

/**
 * LoginServices Class
 * 
 * @author Justin
 * @resources https://www.baeldung.com/udp-in-java
 */

public class Server extends Thread {
	private final int BUF_LENGTH = 256;
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf;
	private DFS dfs;
	private int port, chordPort, chordJoinedTo;

	// Creates server and assigns it to a port, 3000.
	public Server(int port, int chordPort, int portToJoin) throws Exception {
		try {
			dfs = new DFS(chordPort);
			dfs.join("127.0.0.1", portToJoin);
			socket = new DatagramSocket(port);
			this.port = port;
			this.chordPort = chordPort;
			this.chordJoinedTo = portToJoin;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Starts a thread for the server and gets it running
	public void run() {
		running = true;
		this.displayDetails();
		while (running) {
			// This byte buffer may need to be adjusted for our application
			buf = new byte[BUF_LENGTH];
			// Creates a DatagramPacket with an empty byte array and a length of BUF_LENGTH
			DatagramPacket packet = new DatagramPacket(buf, BUF_LENGTH);
			try {
				// Attempt to receive packet
				socket.receive(packet);
				// Pass packet to a runnable object
				ServerThread thread = new ServerThread(packet, buf, socket);
				// Create a new thread from the runnable and start it
				new Thread(thread).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}
	
	public void displayDetails() {
		System.out.println("===================== SERVER DETAILS =====================");
		System.out.println("Status: " + (this.running == true ? "Running" : "Not Running"));
		System.out.println("Port Number: " + this.port);
		System.out.println("This Server's Chord Port: " + this.chordPort);
		System.out.println("This Server's Chord ID: " + this.dfs.getChordGUID());
		System.out.println("This Server's Chord's Info:");
		this.dfs.getChordInfo();
		System.out.println("==========================================================");
	}
}