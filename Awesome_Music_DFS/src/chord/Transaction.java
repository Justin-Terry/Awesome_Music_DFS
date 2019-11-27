package chord;

public class Transaction
{
	public enum Operation { WRITE, DELETE};
	public enum Vote { YES, NO};
	private Long TransactionId;
	private Vote vote;
	private Operation operation;
	private String fileName;
	private Long pageIndex;
	
	public Transaction(Long tId, String fn, Long pIndex) {
		TransactionId = tId;
		fileName = fn;
		pageIndex = pIndex;
		vote = null;
		operation = null;
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

	public Long getPageIndex() {
		return pageIndex;
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
