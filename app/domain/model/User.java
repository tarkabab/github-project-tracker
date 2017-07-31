package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

@Entity
public class User extends Model {
	@Id
	@Constraints.Required
	@Formats.NonEmpty
	public String username;
	@Constraints.Required
	@Valid
	@Embedded
	public Profile profile;
	@Constraints.Required
	@Formats.NonEmpty
	public String password;

	public final static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
	
	public static Optional<User> forId(final String username) {
		return Optional.fromNullable(find.byId(username));
	}

	public final void add() throws UserNameAlreadyTakenException {
		if (forId(username).isPresent()) {
			throw new UserNameAlreadyTakenException("User name " + username + " is already taken");
		} else {
			save();
		}
	}

	@Embeddable
	public static class Profile {

		@Constraints.Email
		@Constraints.Required
		public String email;
		@Constraints.Required
		@Formats.NonEmpty
		public String firstName;
		@Constraints.Required
		@Formats.NonEmpty
		public String lastName;
		@Constraints.Required
		public Integer age;
	}

}