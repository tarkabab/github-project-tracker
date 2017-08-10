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
import java.util.List;

import domain.model.BacklogItem;
import domain.types.Priority;


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
        Optional<BacklogItem> searchResult = BacklogItem.forId(backlogItemId);
		if (searchResult.isPresent()) {
            JsonNode json = request().body().asJson();
            String newSummary = json.path("summary").getTextValue();
            BacklogItem backlogItem = searchResult.get();
            backlogItem.summary = newSummary;
            backlogItem.update();
            return ok(Json.toJson(backlogItem));
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
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public static Result prioritize(Long backlogItemId) {
        Optional<BacklogItem> searchResult = BacklogItem.forId(backlogItemId);
		if (searchResult.isPresent()) {
            JsonNode json = request().body().asJson();
            String parameter = json.path("priority").getTextValue();
            Priority newPriority = Priority.valueOf(parameter);
            if(newPriority == null) {
                return badRequest(Json.toJson(ImmutableMap.of("error", "Unknown priority: " + parameter)));
            }
            BacklogItem backlogItem = searchResult.get();
            backlogItem.priority = newPriority;
            backlogItem.update();
            return ok(Json.toJson(backlogItem));
        } 
        return notFound(Json.toJson(ImmutableMap.of("error", "BacklogItem with id " + backlogItemId + " cannot be found")));
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
            backlogItem.save();
			return created(Json.toJson(backlogItem));
		}
	}

}