package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;


public class Page {
		// [{"guid":"46312", "size": "1024", "creationTS":"1256933732","readTS":"1256953732", "writeTS":"1256953732", "referenceCount
			private long guid;
			private int size;
			private Timestamp creationTS;
			private Timestamp readTS;
			private Timestamp writeTS;
			private int referenceCount;
			
			private long md5(String objectName)
		    {
		        try
		        {
		            MessageDigest m = MessageDigest.getInstance("MD5");
		            m.reset();
		            m.update(objectName.getBytes());
		            BigInteger bigInt = new BigInteger(1,m.digest());
		            return Math.abs(bigInt.longValue());
		        }
		        catch(NoSuchAlgorithmException e)
		        {
		                e.printStackTrace();
		                
		        }
		        return 0;
		    }
			
			Page(long guid, int size, Timestamp creationTS, Timestamp writeTS, int referenceCount)
			{
				this.guid = guid;
				this.size = size;
				this.creationTS = creationTS;
				this.readTS = new Timestamp(System.currentTimeMillis());
				this.writeTS = writeTS;
				this.referenceCount = referenceCount;
			}

			public Page(String name, Timestamp stamp, int size)
			{
				this.guid = md5(name+stamp.toString());
				this.size = size;
				this.creationTS = new Timestamp(System.currentTimeMillis());
				this.readTS = new Timestamp(System.currentTimeMillis());
				this.referenceCount = 0;
			}
			
			
			public long getGuid() {
				return guid;
			}

			public void setGuid(long guid) {
				this.guid = guid;
			}

			public int getSize() {
				return size;
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