package	domain.types;

public class TeamNameAlreadyTakenException extends RuntimeException {
	public TeamNameAlreadyTakenException(final String msg) {
		super(msg);
	}
}