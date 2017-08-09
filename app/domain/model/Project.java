package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

@Entity
public class Project extends Model {

    @Id
    public long id;
	@Constraints.Required
	@Formats.NonEmpty
    public String name;
    public String description;
	@Constraints.Required
	@Formats.NonEmpty
    public long teamId;

    public String githubUrl = "";
    public String gitUrl = "";
    public int githubWatchers;
    public int githubForks;

    public final static Finder<Long, Project> find = new Finder<Long, Project>(Long.class, Project.class);

	public static Optional<Project> forId(final long projectId) {
		return Optional.fromNullable(find.byId(projectId));
	}

}