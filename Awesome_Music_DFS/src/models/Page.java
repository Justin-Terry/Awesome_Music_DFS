package models;

public class Page {
// [{"guid":"46312", "size": "1024", "creationTS":"1256933732","readTS":"1256953732", "writeTS":"1256953732", "referenceCount
	private int guid;
	private int size;
	private int creationTS;
	private int readTS;
	private int writeTS;
	private int referenceCount;
	
	Page(int guid, int size, int creationTS, int readTS, int writeTS, int referenceCount)
	{
		this.guid = guid;
		this.size = size;
		this.creationTS = creationTS;
		this.readTS = readTS;
		this.writeTS = writeTS;
		this.referenceCount = referenceCount;
	}

	public int getGuid() {
		return guid;
	}

	public void setGuid(int guid) {
		this.guid = guid;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCreationTS() {
		return creationTS;
	}

	public void setCreationTS(int creationTS) {
		this.creationTS = creationTS;
	}

	public int getReadTS() {
		return readTS;
	}

	public void setReadTS(int readTS) {
		this.readTS = readTS;
	}

	public int getWriteTS() {
		return writeTS;
	}

	public void setWriteTS(int writeTS) {
		this.writeTS = writeTS;
	}

	public int getReferenceCount() {
		return referenceCount;
	}

	public void setReferenceCount(int referenceCount) {
		this.referenceCount = referenceCount;
	}
	
	
}
