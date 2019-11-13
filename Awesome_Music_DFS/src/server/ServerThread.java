package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
 * @Author Justin Terry
 *
 */

public class ServerThread implements Runnable{
	private DatagramPacket packet;
	private byte[] buf;
	private DatagramSocket socket;

	
	public ServerThread(DatagramPacket packet, byte[] buf, DatagramSocket soc) {
		this.packet = packet;
		this.buf = buf;
		this.socket = soc;
	}

	@Override
	public void run() {
		// Get some info from the packet for the return
		InetAddress address = packet.getAddress();
		int port = packet.getPort(); 

		// Create an instance of the dispatcher and pass it the request
		Dispatcher dis = new Dispatcher();
		String returnString = dis.dispatch(new String(packet.getData()));
		// Add the returned string to the buffer
		buf = new byte[returnString.getBytes().length];
		buf = returnString.getBytes(); 
		// Create the return packet
		packet = new DatagramPacket(buf, buf.length, address, port);

		try {
			// Return the packet
			System.out.println(packet.getLength());
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
