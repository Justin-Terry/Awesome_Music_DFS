package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

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

	// Creates server and assigns it to a port, 3000.
	public Server(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Starts a thread for the server and gets it running
	public void run() {
		System.out.println("Server Up");
		running = true;
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
}