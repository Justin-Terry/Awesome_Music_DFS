// public void move(String oldName, String newName) throws Exception
//    {
//        // TODO:  Change the name in Metadata
//        // Write Metadata
//    	FilesJson file = readMetaData();
//    	for(FileJson files : file.getFile()) {
//    		if(files.getName().equals(oldName)) {
//    			// That file already exists, return
//    			files.setName(newName);
//    			files.setWriteTS(new Timestamp(System.currentTimeMillis()));
//    		}
//    	}
//    }

package chord;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
import java.io.InputStream;
import java.util.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

/* JSON Format
{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
} 
*/

public class DFS {

	public class PagesJson {
		// [{"guid":"46312", "size": "1024",
		// "creationTS":"1256933732","readTS":"1256953732", "writeTS":"1256953732",
		// "referenceCount
		private long guid;
		private long size;
		private Timestamp creationTS;
		private Timestamp readTS;
		private Timestamp writeTS;
		private int referenceCount;

		PagesJson(long guid, long size, int referenceCount, Timestamp creationTS, Timestamp writeTS) {
			this.guid = guid;
			this.size = size;
			this.creationTS = creationTS;
			this.readTS = new Timestamp(System.currentTimeMillis());
			this.referenceCount = referenceCount;
			this.writeTS = writeTS;
		}

		PagesJson(String name, long size) {

			this.creationTS = new Timestamp(System.currentTimeMillis());
			this.guid = md5(name + creationTS.toString());
			this.size = size;
			this.referenceCount = 0;
			readTS = creationTS;
		}
		


		public long getGuid() {
			return guid;
		}

		public void setGuid(long guid) {
			this.guid = guid;
		}

		public long getSize() {
			return this.size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public Timestamp getCreationTS() {
			return creationTS;
		}

		public void setCreationTS(Timestamp creationTS) {
			this.creationTS = creationTS;
		}

		public Timestamp getReadTS() {
			return readTS;
		}

		public void setReadTS(Timestamp readTS) {
			this.readTS = readTS;
		}

		public Timestamp getWriteTS() {
			return writeTS;
		}

		public void setWriteTS(Timestamp writeTS) {
			this.writeTS = writeTS;
		}

		public int getReferenceCount() {
			return referenceCount;
		}

		public void setReferenceCount(int referenceCount) {
			this.referenceCount = referenceCount;
		}
	}

	public class FileJson {
		private String name;
		private Timestamp creationTS;
		private Timestamp readTS;
		private Timestamp writeTS;
		private Long referenceCount;
		private Long numberOfPages;
		private Long maxPageSize;
		private Long size;
		private ArrayList<PagesJson> pages;

		public FileJson(String filename) {
			this.name = filename;
			this.pages = new ArrayList<PagesJson>();
			this.creationTS = Timestamp.from(Instant.now());
			this.referenceCount = 0l;
			this.numberOfPages = 0l;
			this.size = 0l;
			this.maxPageSize = 1024l;
		};

		public FileJson(String filename, ArrayList<PagesJson> pages) {
			this.name = filename;
			this.pages = pages;
			this.creationTS = Timestamp.from(Instant.now());
			this.referenceCount = 0l;
			this.numberOfPages = (long) pages.size();
			this.size = (long) pages.size();
			this.maxPageSize = 1024l;
		}

		public FileJson(String name, Timestamp creationTS, Long referenceCount, Long numberOfPages, Long maxPageSize,
				Long size, ArrayList<PagesJson> pages) {
			this.name = name;
			this.creationTS = creationTS;
			this.referenceCount = referenceCount;
			this.numberOfPages = numberOfPages;
			this.maxPageSize = maxPageSize;
			this.size = size;
			this.pages = pages;
		}

		private long calcSize() {
			long s = 0l;
			for (PagesJson page : pages) {
				s += page.size;
			}
			return s;
		}

		public void addPage(PagesJson page) {
			this.writeTS = Timestamp.from(Instant.now());
			long pageSize = page.getSize();
			this.size += pageSize;
			this.pages.add(page);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Timestamp getCreationTS() {
			return creationTS;
		}

		public void setCreationTS(Timestamp creationTS) {
			this.creationTS = creationTS;
		}

		public Timestamp getReadTS() {
			return readTS;
		}

		public void setReadTS(Timestamp readTS) {
			this.readTS = readTS;
		}

		public Timestamp getWriteTS() {
			return writeTS;
		}

		public void setWriteTS(Timestamp writeTS) {
			this.writeTS = writeTS;
		}

		public Long getReferenceCount() {
			return referenceCount;
		}

		public void setReferenceCount(Long referenceCount) {
			this.referenceCount = referenceCount;
		}

		public Long getNumberOfPages() {
			return numberOfPages;
		}

		public void setNumberOfPages(Long numberOfPages) {
			this.numberOfPages = numberOfPages;
		}

		public Long getMaxPageSize() {
			return maxPageSize;
		}

		public void setMaxPageSize(Long maxPageSize) {
			this.maxPageSize = maxPageSize;
		}

		public Long getSize() {
			return size;
		}

		public void setSize(Long size) {
			this.size = size;
		}

		public ArrayList<PagesJson> getPages() {
			return pages;
		}

		public void setPages(ArrayList<PagesJson> pages) {
			this.pages = pages;
		}

	};

	public class FilesJson {
		List<FileJson> file;

		public FilesJson() {
			file = new ArrayList<FileJson>();
		}

		public int getSize() {
			return file.size();
		}

		public FileJson getFile(int i) {
			return file.get(i);
		}

		public void setFile(List<FileJson> file) {
			this.file = file;
		}

		public void addFileJson(FileJson fileJson) {
			this.file.add(fileJson);
		}
		
		public void display() {
			
		}
		
		public void deleteFile(String fileName) {
			ListIterator<FileJson> i = file.listIterator();
			
			while (i.hasNext()) {
				FileJson tempFile = i.next();
				if (tempFile.getName().equalsIgnoreCase(fileName)) {
					i.remove();
				}
			}
		}
	};

	int port;
	Chord chord;

	private long md5(String objectName) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(objectName.getBytes());
			BigInteger bigInt = new BigInteger(1, m.digest());
			return Math.abs(bigInt.longValue());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

		}
		return 0;
	}
	
	public DFS(int port) throws Exception {

		this.port = port;
		long guid = md5("" + port);
		chord = new Chord(port, guid);
		Files.createDirectories(Paths.get(guid + "/repository"));
		Files.createDirectories(Paths.get(guid + "/tmp"));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				chord.leave();
			}
		});

	}

	/**
	 * Join the chord
	 *
	 */
	public void join(String Ip, int port) throws Exception {
		chord.joinRing(Ip, port);
		chord.print();
	}

	/**
	 * leave the chord
	 *
	 */
	public void leave() throws Exception {
		chord.leave();
	}

	public long getChordGUID() {
		return this.chord.guid;
	}

	public void getChordInfo() {
		this.chord.print();
	}

	/**
	 * print the status of the peer in the chord
	 *
	 */
	public void print() throws Exception {
		chord.print();
	}

	/**
	 * readMetaData read the metadata from the chord
	 *
	 */
	public FilesJson readMetaData() throws Exception {
		FilesJson filesJson = null;
		try {
			Gson gson = new Gson();
			long guid = md5("Metadata");

			System.out.println("GUID " + guid);
			ChordMessageInterface peer = chord.locateSuccessor(guid);
			RemoteInputFileStream metadataraw = peer.get(guid);
			metadataraw.connect();
			Scanner scan = new Scanner(metadataraw);
			scan.useDelimiter("\\A");
			String strMetaData = scan.next();
			System.out.println(strMetaData);
			filesJson = gson.fromJson(strMetaData, FilesJson.class);
		} catch (Exception ex) {
			filesJson = new FilesJson();
		}
		return filesJson;
	}

	/**
	 * writeMetaData write the metadata back to the chord
	 *
	 */
	public void writeMetaData(FilesJson filesJson) throws Exception {
		long guid = md5("Metadata");
		ChordMessageInterface peer = chord.locateSuccessor(guid);

		Gson gson = new Gson();
		peer.put(guid, gson.toJson(filesJson));
	}

	/**
	 * Change Name
	 *
	 */
	public void move(String oldName, String newName) throws Exception {
		FilesJson files = readMetaData();
		boolean checkForFile = false;
		for (int i = 0; i < files.getSize(); i++) {
			if (files.getFile(i).getName().equalsIgnoreCase(oldName)) {
				files.getFile(i).setName(newName);
				files.getFile(i).setWriteTS(Timestamp.from(Instant.now()));
				checkForFile = true;
				
				}
		}
		
		if (checkForFile) {
			this.writeMetaData(files);
			System.out.println(oldName + " has been changed to " + newName);
		}
		else 
			System.out.println(oldName + " does not exist. No action was taken.");

	}

	/**
	 * List the files in the system
	 *
	 * @param filename
	 *            Name of the file
	 */
	public String lists() throws Exception {
		FilesJson fileJson = readMetaData();
		String listOfFiles = fileJson.toString();

		return listOfFiles;
	}

	/**
	 * create an empty file
	 *
	 * @param filename
	 *            Name of the file
	 */
	public void create(String fileName) throws Exception {
		FileJson metaFile = new FileJson(fileName);
		// Get the metadata
		FilesJson file = this.readMetaData();
		// Check if a file with that name already exists
		metaFile.setName(fileName);
		// Add the new file to the metadata and write the metadata to file
		file.addFileJson(metaFile);
		this.writeMetaData(file);
	}

	/**
	 * delete file
	 *
	 * @param filename
	 *            Name of the file
	 */
	public void delete(String fileName) throws Exception {
		FilesJson files = readMetaData();
		for (int i = 0; i < files.getSize(); i++) {
			if (files.getFile(i).getName().equalsIgnoreCase(fileName)) {
				if (files.getFile(i).getNumberOfPages() > 0) {
					FileJson file = files.getFile(i);
					for (int j = 0; j < file.getNumberOfPages(); j ++) {
						PagesJson page = file.pages.get(j);
						long guid = page.guid;
						ChordMessageInterface peer = chord.locateSuccessor(guid);
						peer.delete(guid);
					}
					
				}
				files.deleteFile(fileName);
			}
		}
		this.writeMetaData(files);
	}
	
	

	/**
	 * Read block pageNumber of fileName
	 *
	 * @param filename
	 *            Name of the file
	 * @param pageNumber
	 *            number of block.
	 */
	public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception {
		pageNumber--;
        RemoteInputFileStream InputStream = null;
        FilesJson md = readMetaData();
        for (int i = 0; i < md.getSize(); i++) {
            if (md.getFile(i).getName().equalsIgnoreCase(fileName)) {
                ArrayList<PagesJson> pagesList = md.getFile(i).getPages();
                for (int k = 0; k < pagesList.size(); k++) {
                    if (k == pageNumber) {
                        PagesJson pageToRead = pagesList.get(k);
                        pageToRead.setReadTS(Timestamp.from(Instant.now()));
                        md.getFile(i).setReadTS(Timestamp.from(Instant.now()));
                        Long pageGUID = pageToRead.guid;
                        ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
                        InputStream = peer.get(pageGUID);
                    }
                }
                writeMetaData(md);
            }
        }
        return InputStream;
	}

	/**
	 * Add a page to the file
	 *
	 * @param filename
	 *            Name of the file
	 * @param data
	 *            RemoteInputStream.
	 */
	public void append(String fileName, RemoteInputFileStream data) throws Exception {
		FilesJson theData = this.readMetaData();
		for (int i = 0; i < theData.getSize(); i++) {
			if (theData.getFile(i).getName().toLowerCase().equals(fileName.toLowerCase())) {
				FileJson theFile = theData.getFile(i);
				int dataSize = data.available();
				System.out.println("Datasize: " + dataSize);
				theFile.setWriteTS(Timestamp.from(Instant.now()));
				long pagesGuid = md5(fileName + Timestamp.from(Instant.now()));
				ChordMessageInterface succ = this.chord.locateSuccessor(pagesGuid);
				int point = 0;
				succ.put(pagesGuid, data);
				theFile.addPage(new PagesJson(pagesGuid, dataSize, 0, theFile.getWriteTS(), theFile.getWriteTS()));
			} 
			System.out.println("Appended item " + i);
		}
		this.writeMetaData(theData);

	}

	public static PagesJson PagesJson(String string, Timestamp timestamp, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}