package chord;

public class Transaction
{
	public enum Operation { WRITE, DELETE};
	public enum Vote { YES, NO};
	Long TransactionId;
	Vote vote;
	Operation operation;
	String fileName;
	Long pageIndex;
	
	public Transaction() {
		
	}
	
	public String getOp() {
		return operation.toString();
	}

	

}
