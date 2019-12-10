package chord;

import java.sql.Timestamp;

public class Transaction
{
	public enum Operation { WRITE, DELETE};
	public enum Vote { YES, NO};
	private Long TransactionId;
	private Vote vote;
	private Operation operation;
	private String fileName;
	private Long pageIndex;
	private Timestamp t;
	
	public Transaction(Long tId, String fn ) {
		TransactionId = tId;
		fileName = fn;
		vote = null;
		operation = null;
		t = new Timestamp(System.currentTimeMillis());
	}
	
	public Long getTransactionId() {
		return TransactionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Timestamp getTimestamp() {
		return t;
	}

	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setOp(String o) {
		if(o.compareToIgnoreCase("WRITE") == 0 || o.compareToIgnoreCase("DELETE") == 0) {
			operation = Operation.valueOf(o.toUpperCase());
		}
	}
	
	public String getOp() {
		return operation.toString();
	}
	
	public void setVote(String v) {
		if(v.compareToIgnoreCase("YES") == 0 || v.compareToIgnoreCase("NO") == 0) {
			vote = Vote.valueOf(v.toUpperCase());
		}
	}

	public String getVote() {
		return vote.toString();
	}
	

}
