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
import domain.model.TeamNameAlreadyTakenException;

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
			try {
				team.add();
			} catch (TeamNameAlreadyTakenException ex) {
				return badRequest(Json.toJson(ImmutableMap.of("name", ImmutableList.of(ex.getMessage()))));
			}
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
    public static Result addMember(Long teamId, String memberId) {
        return TODO;
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
        return TODO;
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
		return TODO;
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