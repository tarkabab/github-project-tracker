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

import domain.model.BacklogItem;
public class BacklogItems extends Controller {

    static Form<BacklogItem> backlogItemForm = Form.form(BacklogItem.class);

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
        final Optional<BacklogItem> backlogItem = BacklogItem.forId(backlogItemId);
		if (backlogItem.isPresent()) {
			return ok(Json.toJson(backlogItem.get()));
		} else {
		    return notFound(Json.toJson(ImmutableMap.of("error", "Backlog Item with id " + backlogItemId + " cannot be found")));
        }
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
    
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public static Result changeSummary(Long backlogItemId) {
        Optional<BacklogItem> backlogItem = BacklogItem.forId(backlogItemId);
		if (backlogItem.isPresent()) {
            JsonNode json = request().body().asJson();
            String newSummary = json.path("summary").getTextValue();
            backlogItem.get().summary = newSummary;
            backlogItem.get().save();
            return ok(Json.toJson(backlogItem.get()));
        } 
        return notFound(Json.toJson(ImmutableMap.of("error", "BacklogItem with id " + backlogItemId + " cannot be found")));
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
	@BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public static Result createBacklogItem() {

        final JsonNode json = request().body().asJson();
        final Form<BacklogItem> filled = backlogItemForm.bind(json);

        if (filled.hasErrors()) {
			return badRequest(filled.errorsAsJson());
		} else {
			final BacklogItem backlogItem = filled.get();
            backlogItem.add();
			return created(Json.toJson(backlogItem));
		}
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