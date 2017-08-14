package domain.model;

import play.db.ebean.*;
import play.data.validation.Constraints;
import play.data.format.Formats;
import play.libs.Json;

import com.google.common.base.Optional;

import javax.persistence.*;
import javax.validation.*;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnore;

import domain.model.User;
import domain.types.TeamMember;

@Entity
public class Team extends Model {

    @Id
    public Long id;
	@Constraints.Required
	@Formats.NonEmpty
    public String name;
	@ManyToMany
	public List<User> members = new ArrayList();

	@JsonProperty("members")
	public int getNumberOfMembers() {
		return members.size();
	}

    public final static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);
        
	public static Optional<Team> forId(final long teamId) {
		return Optional.fromNullable(find.byId(teamId));
	}

	public List<TeamMember> addTeamMember(String identity) {
		final Optional<User> searchResult = User.forId(identity);
		if (searchResult.isPresent()) {
			members.add(searchResult.get());
			save();
		}
		return getTeamMembers();
	}

	@JsonIgnore
	public List<TeamMember> getTeamMembers() {
		List<TeamMember> teamMembers = new ArrayList();
		for(User user: members) {
			teamMembers.add(new TeamMember(user.username, user.profile.email, user.profile.firstName + " " + user.profile.lastName));
		}
		return teamMembers;
	}
}