package	domain.types;

public class UserNameAlreadyTakenException extends RuntimeException {
	public UserNameAlreadyTakenException(final String msg) {
		super(msg);
	}
}