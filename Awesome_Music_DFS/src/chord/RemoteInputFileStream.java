package chord;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.*;

public class RemoteInputFileStream extends InputStream implements Serializable {
  
    private InetAddress IP;
    private int port;
    private Long total;
    private Long pos;
    InputStream input;
    private static int BUFFER_LENGTH = 2 << 15;
    /**
     * It stores a buffer with FRAGMENT_SIZE bytes for the current reading. 
     * This variable is useful for UDP sockets. Thus bur is the datagram
     */
    protected byte buf[];
    /**
     * It prepares for the nuext buffer. In UDP sockets you can read nextbufer 
     * while buf is in use
     */
    protected byte nextBuf[];
     /**
     * It is used to read the buffer
     */
    protected int fragment = 0;
    
    public void FileStream(String pathName) throws FileNotFoundException, IOException    {
        File file = new File(pathName);
        total = (int)file.length();	
        pos = 0;		  
    
        ServerSocket serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        port = serverSocket.getInetAddress();
        one = new Thread() {
            public void run() {
                try {
                    
                    Socket socket = serverSocket.accept();
                    FileReader fileReader = new FileReader(pathName);
                    char[] buffer = new char[BUFFER_LENGTH]; 
                    int ret = fileReader.read(buffer, 0, BUFFER_LENGTH);
                    while (ret == BUFFER_LENGTH)
                    {
                        socket.send(buffer);
                        ret = fileReader.read(buffer, 0, BUFFER_LENGTH);
                    }
                    if (ret > 0)
                        socket.send(buffer);
                    fileReader.close();	
                    
                    
                } catch(InterruptedException v) {
                    System.out.println(v);
                }
            }  
        };
    }
    
     /**
     * getNextBuff reads the buffer. It gets the data using
     * the remote method getSongChunk
    */
    protected void getBuff(int fragment) throws IOException
    {
        new Thread()
        {
            public void run() {
                input.read(nextBuf);
                sem.release(); 
                System.out.println("Read buffer");
            }
        }.start();
       
     }
    
    public  RemoteInputFileStream()  {
        this.buf  = new byte[FRAGMENT_SIZE];	
        this.nextBuf  = new byte[FRAGMENT_SIZE];
        pos = 0;
        Socket socket = new Socket(IP, port);
        input = socket.getInputStream();
    }
    
/**
     * Reads the next byte of data from the input stream.
    */
    @Override
    public synchronized int read() throws IOException {
     
     
	  if (pos >= total) 
	  {	
            pos = 0;      
            return -1;
	  }
	  int posmod = pos % BUFFER_LENGTH;
	  if (posmod == 0)
	  {
          try
          {
            sem.acquire(); 
          }catch (InterruptedException exc) 
          { 
                System.out.println(exc);
          }
	      for (int i=0; i< BUFFER_LENGTH; i++)
		      buf[i] = nextBuf[i];
          
	      getBuff(fragment);
	      fragment++;
	  }
	  int p = pos % BUFFER_LENGTH;
	  pos++;
      return buf[p] & 0xff; 
    }
    
    /**
     * Reads some number of bytes from the input stream and stores them
     * into the buffer array b.
    */
    @Override
    public synchronized int read(byte b[], int off, int len)  throws IOException{
        if (b == null) {
            throw new NullPointerException();
	    } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
	    }

	    if (pos >= total) {
     	    return -1;
	    }
	    int avail = total - pos;
	    if (len > avail) {
		  len = avail;
	    }
	    if (len <= 0) {
		  return 0;
	    }
	    for (int i = off; i< off+len;  i++)
		    b[i] = (byte)read();
	    return len;
    }
    
    public int available() throws IOException
    {
	return total - pos;
    }
}