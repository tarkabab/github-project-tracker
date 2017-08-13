package api;

import org.junit.*;
import play.test.*;
import play.libs.Json;
import play.libs.WS;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.ArrayNode;
import com.google.common.collect.*;

import java.util.Arrays;

public class AbstractApiTest {

	static final String baseUrl = "http://localhost:3333/api/";
	static final String usersEndpoint = baseUrl + "users";
	static final String teamsEndpoint = baseUrl + "teams";
	static final String projectsEndpoint = baseUrl + "projects";
	static final String backlogItemsEndpoint = baseUrl + "backlogitems";

	protected final static String TEST_USER_NAME = "john@example.com";
	protected final static String TEST_USER_EMAIL = "john@example.com";

    protected static JsonNode createUserRequest(String username) {
		return createUserRequest(username, TEST_USER_EMAIL);
	}

    protected static JsonNode createUserRequest(String username, String email) {
		return Json.toJson(ImmutableMap.of(
			"username", username,
			"password", "secret",
			"profile", ImmutableMap.of(
				"email", email,
				"firstName", "John",
				"lastName", "Doe",
				"age", 32
				)
			));
    }

	static WS.Response createUser(JsonNode userRequest) {
		return WS.url(usersEndpoint).post(userRequest).get();
	}

	static WS.Response createUser(String username) {
		final JsonNode body = createUserRequest(username);
		return WS.url(usersEndpoint).post(body).get();
	}

	static WS.Response getUserProfile(String username) {
		return WS.url(usersEndpoint + "/" + username + "/profile").get().get();
	}

	static WS.Response updateProfile(String username, String newEmail) {
		final JsonNode body = Json.toJson(ImmutableMap.of(
			"email", newEmail,
			"firstName", "John",
			"lastName", "Doe",
			"age", 30
			));
		return WS.url(usersEndpoint + "/" + username + "/profile").put(body).get();
	}

	static WS.Response createTeam(String name) {
		final JsonNode body = Json.toJson(ImmutableMap.of(
			"name", name));
		return WS.url(teamsEndpoint).post(body).get();
	}

	static WS.Response getTeam(Long teamId) {
		return WS.url(teamsEndpoint + "/" + teamId).get().get();
	}

	static WS.Response createProject(String name, Long teamId) {
		final JsonNode body = Json.toJson(ImmutableMap.of(
			"name", name,
			"description", "The best project in the world",
			"teamId", teamId));
		return WS.url(projectsEndpoint).post(body).get();
	}

	static WS.Response getProject(Long projectId) {
		return WS.url(projectsEndpoint + "/" + projectId).get().get();
	}

	static WS.Response createBacklogItem(String name, Long projectId) {
		final JsonNode body = Json.toJson(ImmutableMap.builder()
			.put("name", name)
			.put("summary", "As a user I want to have a shiny UI")
			.put("itemType", "FEATURE")
			.put("storyPoints", 5)
			.put("priority", "URGENT")
			.put("status", "ESTIMATED")
			.put("tasks", 0)
			.put("projectId", 1L).build());
		return WS.url(backlogItemsEndpoint).post(body).get();
	}

	static WS.Response getBacklogItem(Long backlogItemId) {
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId).get().get();
	}

	static WS.Response getBacklogItemsForProject(Long projectId) {
		return WS.url(projectsEndpoint + "/" + projectId + "/backlogitems").get().get();
	}

	static WS.Response createTask(Long backlogItemId, String name) {
		final JsonNode body = Json.toJson(ImmutableMap.of(
			"name", name,
			"description", "What needs to be done",
			"backlogItemId", backlogItemId));
		final ArrayNode comments = ((ObjectNode)body).putArray("githubComments");
		final JsonNode comment = Json.toJson(ImmutableMap.of(
			"login", "login name",
			"body", "comment body",
			"url", "http://comment.url"));
		comments.add(comment);

		// comments.add("{\"login\":\"logged in commenter\",\"body\":\"comment body\",\"url\":\"http://comment.url\"}");
		// Json.parse("[{\"login\":\"logged in commenter\",\"body\":\"comment body\",\"url\":\"http://comment.url\"}]"));
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/tasks").post(body).get();
	}

	static WS.Response getTask(Long backlogItemId, Long taskId) {
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/tasks/" + taskId).get().get();
	}

	static WS.Response getTasks(Long backlogItemId) {
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/tasks").get().get();
	}
 
}