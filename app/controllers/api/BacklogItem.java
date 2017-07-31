package controllers.api;

import play.mvc.Controller;
import play.mvc.Result;

public class BacklogItem extends Controller {

    /**
     * Returns the Backlog Item with the given id
     *
     * Response:
     *
     * 200
     * {
     *      "id" : 1,
     *      "name" : "Shiny UI",
     *      "summary" : "As a user, I want to have a shiny UI",
     *      "itemType" : "FEATURE",
     *      "storyPoints" : 5,
     *      "priority" : "URGENT",
     *      "status" : "ESTIMATED",
     *      "tasks" : 3,
     *      "projectId" : 1
     * }
     *
     */
	public static Result getBacklogItem(Long backlogItemId) {
		return TODO;
	}

    /**
     * Returns the Task with the given id, belonging to the specified Backlog Item (Tasks are not globally unique,
     * only in the context of their Backlog Item resource)
     *
     * Response:
     *
     * 200
     * {
     *      "id" : 1,
     *      "name" : "Set up project",
     *      "description" : "Set up the build file with all dependencies",
     *      "backlogItemId" : 1,
     *      "githubStatus" : "OPEN",
     *      "githubUrl" : "https://github.com/octocat/Hello-World/issues/1347",
     *      "githubComments" : [
     *          {
     *              "login" : "johndoe",
     *              "body" : "This is more difficult than expected",
     *              "url" : "https://github.com/octocat/Hello-World/issues/1347#issuecomment-1"
     *          }
     *       ]
     * }
     *
     */
	public static Result getTask(Long backlogItemId, Long taskId) {
		return TODO;
	}

    /**
     * Returns all Tasks belonging to this Backlog Item
     *
     * Response:
     * 200
     * [{
     *      "id" : 1,
     *      "name" : "Set up project",
     *      "description" : "Set up the build file with all dependencies",
     *      "backlogItemId" : 1,
     *      "githubStatus" : "OPEN",
     *      "githubUrl" : "https://github.com/octocat/Hello-World/issues/1347",
     *      "githubComments" : [
     *          {
     *              "login" : "johndoe",
     *              "body" : "This is more difficult than expected",
     *              "url" : "https://github.com/octocat/Hello-World/issues/1347#issuecomment-1"
     *          }
     *      ]
     * }]
     *
     */
	public static Result getTasks(Long backlogItemId) {
		return TODO;
	}

    /**
     * Replaces the summary of the Backlog Item with the given id with a new one
     *
     * Input:
     *
     * { "summary" : "my new summary" }
     *
     * Response:
     *
     * 200, empty
     */
	public static Result changeSummary(Long backlogItemId) {
		return TODO;
	}

    /**
     * Prioritizes the Backlog Item with the given id
     *
     * Input:
     *
     * { "priority" : "LOW" }
     *
     * Response:
     *
     * 200, empty
     */
	public static Result prioritize(Long backlogItemId) {
		return TODO;
	}

    /**
     * Creates a new Backlog Item
     *
     * Input:
     *
     * {
     *      "name" : "Shiny UI",
     *      "summary" : "As a user, I want to have a shiny UI",
     *      "itemType" : "FEATURE",
     *      "storyPoints" : 5,
     *      "priority" : "URGENT",
     *      "projectId" : 1
     * }
     *
     * Response:
     *
     * 201
     * {
     *      "id" : 1,
     *      "name" : "Shiny UI",
     *      "summary" : "As a user, I want to have a shiny UI",
     *      "itemType" : "FEATURE",
     *      "storyPoints" : 5,
     *      "priority" : "URGENT",
     *      "status" : "ESTIMATED",
     *      "tasks" : 0,
     *      "projectId" : 1
     * }
     */
	public static Result createBacklogItem() {
		return TODO;
	}

    /**
     * Creates a new Task for the given Backlog Item, as well as a GitHub issue linked to that task in the
     * GitHub repository that is linked to the Project of the Backlog Item
     *
     * Input:
     *
     * {
     *      "name" : "Set up project",
     *      "description" : "Set up the build file with all dependencies",
     *      "backlogItemId" : 1
     * }
     *
     * Response:
     *
     * 201
     * {
     *      "id" : 1,
     *      "name" : "Set up project",
     *      "description" : "Set up the build file with all dependencies",
     *      "backlogItemId" : 1,
     *      "githubStatus" : "OPEN",
     *      "githubUrl" : "https://github.com/octocat/Hello-World/issues/1347",
     *      "githubComments" : []
     * }
     *
     */
	public static Result createTask(Long backlogItemId) {
		return TODO;
	}

}