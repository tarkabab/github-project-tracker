package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

@Entity
public class Team extends Model {

    @Id
    public Long id;
	@Constraints.Required
	@Formats.NonEmpty
    public String name;
    public int members;

    public final static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);
        
    public static Optional<Team> forName(final String name) {
		return Optional.fromNullable(find.where().eq("name", name).findUnique());
    }
    
    public final void add() throws TeamNameAlreadyTakenException {
		if (forName(name).isPresent()) {
			throw new TeamNameAlreadyTakenException("Team name " + name + " is already taken");
		} else {
			save();
		}
	}

}