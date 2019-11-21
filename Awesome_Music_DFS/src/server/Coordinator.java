package server;

import java.rmi.RemoteException;

import chord.Chord;
import chord.ChordMessageInterface;

public class Coordinator extends Chord implements ChordMessageInterface{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Coordinator(int port, long guid) throws RemoteException {
		super(port, guid);
	}
	
	public boolean canCommit(Transaction t)
	{
		return false;
	}
	
	public void doCommit(Transaction t)
	{
		
	}
	
	public void doAbort(Transaction t)
	{
		
	}
}

class Transaction
{
	public enum Operation { WRITE, DELETE};
	public enum Vote { YES, NO};
	Long TransactionId;
	Vote vote;
	String fileName;
	Long pageIndex;
}
