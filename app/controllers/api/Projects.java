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

import domain.model.Project;

public class Projects extends Controller {

    static Form<Project> projectForm = Form.form(Project.class);

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
	@BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public static Result createProject() {

		final JsonNode json = request().body().asJson();
		final Form<Project> filled = projectForm.bind(json);
		if (filled.hasErrors()) {
			return badRequest(filled.errorsAsJson());
		} else {
			final Project project = filled.get();
            project.save();
			return created(Json.toJson(project));
		}
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
     
        final Optional<Project> project = Project.forId(projectId);
		if (project.isPresent()) {
			return ok(Json.toJson(project.get()));
		} else {
		    return notFound(Json.toJson(ImmutableMap.of("error", "Project with id " + projectId + " cannot be found")));
		}
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
	@BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public static Result updateDescription(Long projectId) {
        Optional<Project> project = Project.forId(projectId);
		if (project.isPresent()) {
            JsonNode json = request().body().asJson();
            String newDescription = json.path("description").getTextValue();
            project.get().description = newDescription;
            project.get().save();
            return ok(Json.toJson(project.get()));
        } 
        return notFound(Json.toJson(ImmutableMap.of("error", "Project with id " + projectId + " cannot be found")));
	}

}