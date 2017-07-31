package controllers.api;

import play.mvc.Controller;
import play.mvc.Result;

public class Team extends Controller {

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
	public static Result createTeam() {
		return TODO;
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