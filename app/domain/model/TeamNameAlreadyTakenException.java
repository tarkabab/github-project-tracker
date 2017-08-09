package	domain.model;

public class TeamNameAlreadyTakenException extends RuntimeException {
	public TeamNameAlreadyTakenException(final String msg) {
		super(msg);
	}
}