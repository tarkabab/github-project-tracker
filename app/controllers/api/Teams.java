package controllers.api;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import play.data.Form;
import play.libs.Json;
import play.db.ebean.Transactional;
import org.codehaus.jackson.JsonNode;
import com.google.common.collect.*;
import com.google.common.base.Optional;

import domain.model.Team;
import domain.model.User;

public class Teams extends Controller {

    static Form<Team> teamForm = Form.form(Team.class);

    /**
     * Creates a new Team
     *
     * Input:
     *
     * { "name": "Team Foo" }
     *
     * Response:
     *
     * 201, { "id": 1, "name": "Team Foo", "members": 0 }
     *
     * This is one of the required endpoints.
     *
     */
    @BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public static Result createTeam() {

		final JsonNode json = request().body().asJson();
        final Form<Team> filled = teamForm.bind(json);

        if (filled.hasErrors()) {
			return badRequest(filled.errorsAsJson());
		} else {
			final Team team = filled.get();
            team.save();
			return created(Json.toJson(team));
		}
	}

    /**
     * Adds the Team Member to the Team if not already present
     *
     * Input:
     *
     * { "identity" : "johndoe" }
     *
     * Response:
     *
     * 201
     *
     * [{"identity" : "johndoe", "email" : "john@example.com", "name" : "John Doe"}]
     *
     * (the new list of team members)
     *
     * This is one of the required endpoints.
     *
     */
    @BodyParser.Of(BodyParser.Json.class)
	@Transactional
    public static Result addMember(Long teamId) {
		final Optional<Team> teamSearch = Team.forId(teamId);
		if (teamSearch.isPresent()) {
            String memberId = request().body().asJson().path("identity").getTextValue();
            final Optional<User> userSearch = User.forId(memberId);
		    if (userSearch.isPresent()) {
			    return created(Json.toJson(teamSearch.get().addTeamMember(memberId)));
            } else {
                return notFound(Json.toJson(ImmutableMap.of("error", "User with id " + memberId + " cannot be found")));
            }
		} else {
			return notFound(Json.toJson(ImmutableMap.of("error", "Team with id " + teamId + " cannot be found")));
		}
    }

    /**
     * Returns the Team Members of the Team with the given id
     *
     * Response:
     *
     * 200
     * [{"identity" : "johndoe", "email" : "john@example.com", "name" : "John Doe"}]
     *
     * This is one of the required endpoints.
     *
     */
    public static Result getMembers(Long teamId) {
		final Optional<Team> teamSearch = Team.forId(teamId);
		if (teamSearch.isPresent()) {
			return ok(Json.toJson(teamSearch.get().getTeamMembers()));
		} else {
			return notFound(Json.toJson(ImmutableMap.of("error", "Team with id " + teamId + " cannot be found")));
		}
    }

    /**
     * Returns the Team with the given id
     *
     * Response:
     *
     * 200
     * {"id": 1, "name": "Team Foo", "members": 0}
     *
     */
	public static Result getTeam(Long teamId) {
		final Optional<Team> team = Team.forId(teamId);
		if (team.isPresent()) {
			return ok(Json.toJson(team.get()));
		} else {
			return notFound(Json.toJson(ImmutableMap.of("error", "Team with id " + teamId + " cannot be found")));
		}
    }

    /**
     * Removes the Team Member from the given Team if they are in the team
     *
     * Response:
     * 200
     * [] (the new list of team members, in this example an empty JSON array
     *
     */
	public static Result removeMember(Long teamId, String memberId) {
		return TODO;
	}


}