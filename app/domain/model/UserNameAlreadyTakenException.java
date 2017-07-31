package	domain.model;

public class UserNameAlreadyTakenException extends RuntimeException {
	public UserNameAlreadyTakenException(final String msg) {
		super(msg);
	}
}