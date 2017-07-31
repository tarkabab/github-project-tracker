package api;

import org.junit.*;
import play.test.*;
import play.libs.Json;
import play.libs.WS;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.codehaus.jackson.JsonNode;
import com.google.common.collect.*;

public class AbstractApiTest {

	static final String baseUrl = "http://localhost:3333/api/";
	static final String usersEndpoint = baseUrl + "users";
	static final String teamsEndpoint = baseUrl + "teams";
	static final String projectsEndpoint = baseUrl + "projects";
	static final String backlogItemsEndpoint = baseUrl + "backlogitems";

	static WS.Response createUser(String username) {
		final JsonNode body = Json.toJson(ImmutableMap.of(
			"username", username,
			"password", "secret",
			"profile", ImmutableMap.of(
				"email", "john@example.com",
				"firstName", "John",
				"lastName", "Doe",
				"age", 30
				)
			));
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
			.put("name", "As a user I want to have a shiny UI")
			.put("summary", "modified user story")
			.put("itemType", "FEATURE")
			.put("storyPoints", 5)
			.put("priority", "URGENT")
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
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/tasks").post(body).get();
	}

	static WS.Response getTask(Long backlogItemId, Long taskId) {
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/tasks/" + taskId).get().get();
	}

	static WS.Response getTasks(Long backlogItemId) {
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/tasks").get().get();
	}
 
}