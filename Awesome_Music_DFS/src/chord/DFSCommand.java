package chord;


import java.io.*;
import com.google.gson.*;
import com.google.gson.stream.*;

import chord.DFS.PagesJson;
import models.Record;
import server.Server;
//import server.SongLibrary;

import java.sql.Timestamp;



public class DFSCommand
{
    DFS dfs;
        
    public DFSCommand(int p, int portToJoin) throws Exception {
    	  
        dfs = new DFS(p);
        
        if (portToJoin > 0)
        {
            System.out.println("Joining "+ portToJoin);
            dfs.join("127.0.0.1", portToJoin);            
        }
        
        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        String line = buffer.readLine();  
        while (!line.equals("quit"))
        {
            String[] result = line.split("\\s");
            if (result[0].equals("join")  && result.length > 1)
            {
                dfs.join("127.0.0.1", Integer.parseInt(result[1]));     
            }
            if (result[0].equals("print"))
            {
                dfs.print();     
            }
            if (result[0].equals("ls"))
            {
                System.out.println(dfs.lists());     
            }
            
            if (result[0].equals("leave"))
            {
                dfs.leave();     
            }
            if(result[0].equals("test")) {
            	// Perform test
            }
            if(result[0].equals("create") && result.length > 1) {
            	dfs.create(result[1]);
            }
            if(result[0].equals("append")) {
            	RemoteInputFileStream ris = new RemoteInputFileStream(result[1]);
            	dfs.append(result[1], ris);
            }
            if(result[0].equals("move") && result.length > 2) {
            	dfs.move(result[1], result[2]);
            }
            if(result[0].equals("delete") && result.length > 1) {
            	dfs.delete(result[1]);
            }
            if(result[0].equals("pull")){
            	dfs.pull();
            }
            if(result[0].equals("push")) {
            	// Pass the file name that is going to be pushed (i.e. the whole file system)
            	dfs.push();
            }
            
            line=buffer.readLine();
            
            
        }
            // User interface:
            // join, ls, touch, delete, read, tail, head, append, move
    }
    
    static public void main(String args[]) throws Exception
    {
        Gson gson = new Gson();
        RemoteInputFileStream in = new RemoteInputFileStream("./src/resources/music.json", false);
        in.connect();
        Reader targetReader = new InputStreamReader(in);
        JsonReader jreader = new  JsonReader(targetReader);
        Record[] music = gson.fromJson(jreader, Record[].class);
        
        if (args.length < 1 ) {
            throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
        }
        if (args.length > 1 ) {
            DFSCommand dfsCommand=new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
        else
        {
            DFSCommand dfsCommand=new DFSCommand( Integer.parseInt(args[0]), 0);
        }
        
        
     } 
}
