package chord;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;
import java.io.*;

/** 
 * Chord extends from UnicastRemoteObject to support RMI. 
 * It implements the ChordMessageInterface
 *
 */
public class Chord extends java.rmi.server.UnicastRemoteObject implements ChordMessageInterface
{
    // Numbers of fingers
    public static final int M = 2;     
    
     // rmi registry for lookup the remote objects.
    Registry registry;  
    // Successor peeer
    ChordMessageInterface successor;  
    // Predecessor peeer
    ChordMessageInterface predecessor; 
    // array of fingers
    ChordMessageInterface[] finger;	    
    // it is used to keep the fingers updated
    int nextFinger;			            	
    // GUID
    long guid;   			            
    
/**
 * Constructor of the Chord.                 
 * <p>
 * The function is used to debug if the ring is correctly formed
 * </p>
  *
 * @param  port it is the port where it listen. If the port is being used 
 * for another process, it throw RemoteException.    
 * @param guid the global unique id of the peer. 
 */
    public Chord(int port, long guid) throws RemoteException {
        int j;
        // Initialize the variables
	    finger = new ChordMessageInterface[M];
        for (j=0;j<M; j++){
	       finger[j] = null;
     	}
        this.guid = guid;
	
        predecessor = null;
        successor = this;
        Timer timer = new Timer();
        // It sets the timer to self stabilize the chord when nodes leave or join
        timer.scheduleAtFixedRate(new TimerTask() {
	    @Override
	    public void run() {
            stabilize();
            fixFingers();
            checkPredecessor();
            }
        }, 500, 500);   // Every half a second
        try{
            // create the registry and bind the name and object.
            System.out.println(guid + " is starting RMI at port="+port);
            registry = LocateRegistry.createRegistry( port );
            registry.rebind("Chord", this);
        }
        catch(RemoteException e){
	       throw e;
        } 
    }
    
    

/**
 * return true if the key is in the open interval (key1, key2)
 */    
    public Boolean isKeyInOpenInterval(long key, long key1, long key2)
    {
      if (key1 < key2)
          return (key > key1 && key < key2);
      else
          return (key > key1 || key < key2);
    }
    
    
/**
 *return true if the key is in the semi-open interval (key1, key2]
 */
    public Boolean isKeyInSemiCloseInterval(long key, long key1, long key2)
    {
      return isKeyInOpenInterval(key, key1, key2) || key == key2;
    }
    
/** 
 *  put a file in the repository
 */    
    public void put(long guidObject, InputStream stream) throws RemoteException {
      try {
          String fileName = "./"+guid+"/repository/" + guidObject;
          FileOutputStream output = new FileOutputStream(fileName);
          while (stream.available() > 0)
              output.write(stream.read());
          output.close();
      }
      catch (IOException e) {
          System.out.println(e);
      }
    }
    
/**
 * return the file guidObject
 */
    public InputStream get(long guidObject) throws RemoteException {
        FileStream file = null;
        try {
             file = new FileStream("./"+guid+"/repository/" + guidObject);
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return file;
    }
    
    
/**
 * deletes a file with guidObject from the repository
 */
    public void delete(long guidObject) throws RemoteException {
        File file = new File("./"+guid+"/repository/" + guidObject);
        file.delete();
    }
    
/**
 * returns the id of the peer
 */
    public long getId() throws RemoteException {
        return guid;
    }
    
/**
 * It is used to detect that the peer is still alive
 * <p>
 * return true
 */
    public boolean isAlive() throws RemoteException {
	    return true;
    }
    
/**
 * return the predecessor
 * <p>
 * return the Chord Interface of the predecessor
 */
    public ChordMessageInterface getPredecessor() throws RemoteException {
	    return predecessor;
    }

/**
 * locates the successor of key 
 * <p>
 * @param key 
 * return the Chord Interface of the successor of key
 */ 
    public ChordMessageInterface locateSuccessor(long key) throws RemoteException {
	    if (key == guid)
            throw new IllegalArgumentException("Key must be distinct that  " + guid);
	    if (successor.getId() != guid)
	    {
	      if (isKeyInSemiCloseInterval(key, guid, successor.getId()))
	        return successor;
	      ChordMessageInterface j = closestPrecedingNode(key);
	      
          if (j == null)
	        return null;
	      return j.locateSuccessor(key);
        }
        return successor;
    }
    
/**
 * Returns the closest preceding node for the key
 * <p>
 * @param key 
 * return the Chord Interface of the closet preceding node
 */ 
    public ChordMessageInterface closestPrecedingNode(long key) throws RemoteException {
        if(key != guid) {
            int i = M - 1;
            while (i >= 0) {
                try{
                
                    // It verifies from the largest interval
                    if(finger[i] != null && isKeyInSemiCloseInterval(finger[i].getId(), guid, key)) {
                        if(finger[i].getId() != key)
                            return finger[i];
                        else {
                            return successor;
                        }
                    }
                }
                catch(Exception e)
                {
                    // Skip ;
                }
                i--;
            }
        }
        return successor;
    }
    
/**
 * It joins the ring in the peer (ip,port). The peer must exist
 * <p>
 * @param ip of the peer
 * @param port of the peer
 */ 
    public void joinRing(String ip, int port)  throws RemoteException {
        try{
            System.out.println("Get Registry to joining ring");
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ChordMessageInterface chord = (ChordMessageInterface)(registry.lookup("Chord"));
            predecessor = null;
            successor = chord.locateSuccessor(this.getId());
            System.out.println("Joining ring");
        }
        catch(RemoteException | NotBoundException e){
            successor = this;
        }   
    }
  
/**
 *  If the successor fails, it tries to handle the failure using the 
 * first finger available
 * <p>
 */ 
    public void findingNextSuccessor()
    {
        int i;
        successor = this;
        for (i = 0;  i< M; i++)
        {
            try
            {
                if (finger[i].isAlive())
                {
                    successor = finger[i];
                }
            }
            catch(RemoteException | NullPointerException e)
            {
                finger[i] = null;
            }
        }
    }
    
/**
 * Stabilizes the chord 
 * <p>
 * It verifies if the peer is in the right interval. If it is not 
 * in the interval, it corrects the interval
 * This method executed by the timer. 
 */ 
    public void stabilize() {
      try {
          if (successor != null)
          {
              ChordMessageInterface x = successor.getPredecessor();
	   
              // It verifies if the predecessor is in the correct interval 
              // x.getId() != this.getId() is used for for the trivial case
              // where only one peer exists
              if (x != null && x.getId() != this.getId() && isKeyInOpenInterval(x.getId(), this.getId(), successor.getId()))
              {
                  successor = x;
              }
              // The if statament is to handle the trivial case where only 
              // one peer exists
              if (successor.getId() != getId())
              {
                  // We notified the successor that there It verifies if the predecessor is in the correct interval 
                  successor.notify(this);
              }
          }
      } catch(RemoteException | NullPointerException e1) {
          findingNextSuccessor();

      }
    }
    
/**
 * A node notifies that it has a new predecessor j. It also moves all the 
 * files that the predecessor must handle
 * </p>
 * @param j the new predecessor
 */
    public void notify(ChordMessageInterface j) throws RemoteException {
         if (predecessor == null || (predecessor != null
                    && isKeyInOpenInterval(j.getId(), predecessor.getId(), guid)))
             predecessor = j;
            try {
                File folder = new File("./"+guid+"/repository/");
                // It reads all the files in repository
                File[] files = folder.listFiles();
                
                for (File file : files) {
                    long guidObject = Long.valueOf(file.getName());
                    // If the guidObject is less than the new predecessor
                    if(guidObject < predecessor.getId() && predecessor.getId() < guid) {
                        predecessor.put(guidObject, new FileStream(file.getPath()));
                        file.delete();
                    }
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                //happens sometimes when a new file is added during the loop
            } catch (IOException e) {
            e.printStackTrace();
        }

    }
  
/**
 * Fixes the fingers 
 * <p>
 * Every time that is executed is fixing the Finger nextFinger.
 * This method executed by the timer. 
 */ 
    public void fixFingers() {
    
        long id= guid;
        try {
            // The finger is at distance 2^(nextFinger) of this.getId()
            // We use a shift to the left to perform the operation
            long nextId = this.getId() + (1<< (nextFinger+1));
            finger[nextFinger] = locateSuccessor(nextId);
	    
            // The same process cannot be a finger
            if (finger[nextFinger].getId() == guid)
                finger[nextFinger] = null;
            else
                nextFinger = (nextFinger + 1) % M;
        }
        catch(RemoteException | NullPointerException e){
             System.out.println(e.getMessage());
             e.printStackTrace();
        }
    }
 
 /**
 * It checks if the predecessor is still alive. 
 * <p>
 * It checks if the predecessor is still alive. If the predecessor 
 * is not present it sets its predecessor to null. This method executed
 * by the timer. 
 */   
    public void checkPredecessor() { 	
      try {
          if (predecessor != null && !predecessor.isAlive())
              predecessor = null;
      } 
      catch(RemoteException e) 
      {
          predecessor = null;
//           e.printStackTrace();
      }
    }
       
    
/**
 * Prints the successor, predecessor and fingers if they are not null.                 
 * <p>
 * The function is used to debug if the ring is correctly formed
 * </p>
 */
    void print()
    {   
        int i;
        try {
            if (successor != null)
                System.out.println("successor "+ successor.getId());
            if (predecessor != null)
                System.out.println("predecessor "+ predecessor.getId());
            for (i=0; i<M; i++)
            {
                try {
                    if (finger[i] != null)
                        System.out.println("Finger "+ i + " " + finger[i].getId());
                } catch(NullPointerException e)
                {
                    System.out.println("Cannot retrive id of the finger " + i);   
                }
            }
        }
        catch(RemoteException e){
	       System.out.println("Cannot retrive id of successor or predecessor");
        }
    }
}
