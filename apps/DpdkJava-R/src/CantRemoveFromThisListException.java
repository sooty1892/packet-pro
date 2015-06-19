
public class CantRemoveFromThisListException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CantRemoveFromThisListException() {
		super();
	}
	 
	public CantRemoveFromThisListException(String message) {
		super(message);
	}
	
	public CantRemoveFromThisListException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CantRemoveFromThisListException(Throwable cause) {
		super(cause);
	}

}