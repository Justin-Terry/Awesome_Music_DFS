package server;

import java.rmi.RemoteException;

import chord.Chord;
import chord.ChordMessageInterface;
import chord.Transaction;
import chord.Transaction.Vote;

public class Coordinator extends Chord implements ChordMessageInterface{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Coordinator(int port, long guid) throws RemoteException {
		super(port, guid);
	}
	
	public Vote canCommit(Transaction t)
	{
		return Vote.valueOf("NO");
	}
	
	public void doCommit(Transaction t)
	{
		
	}
	
	public void doAbort(Transaction t)
	{
		
	}
}

