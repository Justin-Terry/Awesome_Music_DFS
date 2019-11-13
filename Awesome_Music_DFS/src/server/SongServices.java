package server;
/**
* SongDispatcher is the main responsable for obtaining the songs 
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Base64;
import java.io.FileNotFoundException;


public class SongServices
{
    static final int FRAGMENT_SIZE = 8192; 
    public SongServices()
    {
        
    }
    
    /* 
    * getSongChunk: Gets a chunk of a given song
    * @param key: Song ID. Each song has a unique ID 
    * @param fragment: The chunk corresponds to 
    * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
    */
    public String getSongChunk(String key, String fragment) throws FileNotFoundException, IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];

        File file = new File("./src/resources/" + key);
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.skip(Long.valueOf(fragment) * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close(); 
        // Encode in base64 so it can be transmitted 
         return Base64.getEncoder().encodeToString(buf); 
    }
    
//    public byte[] getSongChunk(Long key, Long fragment) throws FileNotFoundException, IOException
//    {
//        byte buf[] = new byte[FRAGMENT_SIZE];
//
//        File file = new File("./src/resources/" + key);
//        FileInputStream inputStream = new FileInputStream(file);
//        inputStream.skip(fragment * FRAGMENT_SIZE);
//        inputStream.read(buf);
//        inputStream.close(); 
//        // Encode in base64 so it can be transmitted 
//         return buf;
//    }
    
    /* 
    * getFileSize: Gets a size of the file
    * @param key: Song ID. Each song has a unique ID 
     */
    public String getFileSize(String key) throws FileNotFoundException, IOException
    {
        File file = new File("./src/resources/" + key);        
        long total =  file.length();
        System.out.println("file " + key + " has length " + total);
        return Long.toString(total);
    }
    
}
