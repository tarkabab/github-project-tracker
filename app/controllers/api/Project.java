package controllers.api;

import play.mvc.Controller;
import play.mvc.Result;

public class Project extends Controller {

    /**
     * Creates a new Project, as well as a new GitHub repository linked to that project
     *
     * Input:
     *
     * {
     *      "name" : "Yet Another To-Do App",
     *      "description" : "What the world needed",
     *      "teamId" : 1
     * }
     *
     * Response:
     *
     * 201
     * {
     *      "id" : 1,
     *      "name" : "Yet Another To-Do App",
     *      "description" : "What the world needed",
     *      "teamId" : 1,
     *      "githubUrl" : "https://github.com/johndoe/yet-another-to-do-app-23",
     *      "gitUrl" : "git://github.com/johndoe/yet-another-to-do-app-23",
     *      "githubWatchers" : 16,
     *      githubForks" : 3
     * }
     *
     * This is one of the required endpoints.
     */
	public static Result createProject() {
		return TODO;
	}

    /**
     * Returns the Project with the given id
     *
     * Response:
     *
     * 200
     * {
     *      "id" : 1,
     *      "name" : "Yet Another To-Do App",
     *      "description" : "What the world needed",
     *      "teamId" : 1,
     *      "githubUrl" : "https://github.com/johndoe/yet-another-to-do-app-23",
     *      "gitUrl" : "git://github.com/johndoe/yet-another-to-do-app-23",
     *      "githubWatchers" : 16,
     *      "githubForks" : 3
     * }
     *
     * This is one of the required endpoints.
     *
     */
	public static Result getProject(Long projectId) {
		return TODO;
	}

    /**
     * Returns all Backlog Items of the Project with the given id, ordered by ascending priority
     *
     * Response:
     *
     * 200
     * [{
     *      "id" : 1,
     *      "name" : "Shiny UI",
     *      "summary" : "As a user, I want to have a shiny UI",
     *      "itemType" : "FEATURE",
     *      "storyPoints" : 5,
     *      "priority" : "URGENT",
     *      "status" : "ESTIMATED",
     *      "tasks" : 0,
     *      "projectId" : 1
     * }]
     *
     */
	public static Result getBacklogItems(Long projectId) {
		return TODO;
	}

    /**
     * Replaces the current description of the Project with the given id with a new one
     *
     * Input:
     *
     * {
     *      "description" : "What the world really needed",
     * }
     *
     */
	public static Result updateDescription(Long projectId) {
		return TODO;
	}

}