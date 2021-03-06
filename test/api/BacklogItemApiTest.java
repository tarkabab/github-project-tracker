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

import domain.types.Priority;

public class BacklogItemApiTest extends AbstractApiTest {

	@Test
	public void canGetACreatedBacklogItem() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Todo App 3.0", 1L).getStatus()).isEqualTo(CREATED);
				final WS.Response createResponse = createBacklogItem("some stuff", 1L);
				assertThat(createResponse.getStatus()).isEqualTo(CREATED);
				final JsonNode json = Json.toJson(ImmutableMap.builder()
					.put("id", 1L)
					.put("name", "some stuff")
					.put("summary", "As a user I want to have a shiny UI")
					.put("itemType", "FEATURE")
					.put("storyPoints", 5)
					.put("priority", "URGENT")
					.put("status", "ESTIMATED")
					.put("tasks", 0)
					.put("projectId", 1L).build());
				assertThat(json.equals(createResponse.asJson()));
				final WS.Response getResponse = getBacklogItem(1L);
				assertThat(getResponse.getStatus()).isEqualTo(OK);
				assertThat(json.equals(getResponse.asJson()));
			}
		});
	}

	@Test
	public void updatedSummaryIsRepresentedInResource() {
		running(testServer(3333), new Runnable() {
			public void run() {	
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Todo App 3.0", 1L).getStatus()).isEqualTo(CREATED);
				assertThat(createBacklogItem("some stuff", 1L).getStatus()).isEqualTo(CREATED);
				final WS.Response updateResponse = changeSummary(1L, "modified user story");
				assertThat(updateResponse.getStatus()).isEqualTo(OK);
				final JsonNode json = Json.toJson(ImmutableMap.builder()
					.put("id", 1L)
					.put("name", "some stuff")
					.put("summary", "modified user story")
					.put("itemType", "FEATURE")
					.put("storyPoints", 5)
					.put("priority", "URGENT")
					.put("status", "ESTIMATED")
					.put("projectId", 1L).build());
				final WS.Response getResponse = getBacklogItem(1L);
				assertThat(getResponse.getStatus()).isEqualTo(OK);
				assertThat(json.equals(getResponse.asJson()));
			}
		});
	}

	@Test
	public void updatedPriorityIsRepresentedInResource() {
		final String STATUS_BEFORE_UPDATE = "URGENT";
		final String STATUS_AFTER_UPDATE = "LOW";

		running(testServer(3333), new Runnable() {
			public void run() {	
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Todo App 3.0", 1L).getStatus()).isEqualTo(CREATED);
				assertThat(createBacklogItem("some stuff", 1L).getStatus()).isEqualTo(CREATED);

				final WS.Response getBeforeUpdateResponse = getBacklogItem(1L);
				assertThat(getBeforeUpdateResponse.asJson().path("priority").getTextValue()).isEqualTo(STATUS_BEFORE_UPDATE);
				
				final WS.Response updateResponse = prioritize(1L, Priority.valueOf(STATUS_AFTER_UPDATE));
				assertThat(updateResponse.getStatus()).isEqualTo(OK);
				final JsonNode json = Json.toJson(ImmutableMap.builder()
					.put("id", 1L)
					.put("name", "some stuff")
					.put("summary", "As a user I want to have a shiny UI")
					.put("itemType", "FEATURE")
					.put("storyPoints", 5)
					.put("priority", STATUS_AFTER_UPDATE)
					.put("status", "ESTIMATED")
					.put("tasks", 0)
					.put("projectId", 1L).build());
				final WS.Response getAfterUpdateResponse = getBacklogItem(1L);
				assertThat(getAfterUpdateResponse.getStatus()).isEqualTo(OK);
				assertThat(json.equals(getAfterUpdateResponse.asJson()));
			}
		});
	}

	@Test
	public void createdBacklogItemsCanBeFoundByProject() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Todo App 3.0", 1L).getStatus()).isEqualTo(CREATED);
				assertThat(createBacklogItem("some stuff", 1L).getStatus()).isEqualTo(CREATED);
				final WS.Response response = getBacklogItemsForProject(1L);
				assertThat(response.getStatus()).isEqualTo(OK);
				final JsonNode json = Json.toJson(ImmutableList.of(
					ImmutableMap.builder()
						.put("id", 1L)
						.put("name", "some stuff")
						.put("summary", "As a user I want to have a shiny UI")
						.put("itemType", "FEATURE")
						.put("storyPoints", 5)
						.put("priority", "URGENT")
						.put("status", "ESTIMATED")
						.put("tasks", 0)
						.put("projectId", 1L).build()));
				assertThat(json.equals(response.asJson()));
			}
		});
	}

	@Test
	public void canGetACreatedTask() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Todo App 3.0", 1L).getStatus()).isEqualTo(CREATED);
				assertThat(createBacklogItem("some stuff", 1L).getStatus()).isEqualTo(CREATED);
				final WS.Response createResponse = createTask(1L, "set up project");
				assertThat(createResponse.getStatus()).isEqualTo(CREATED);
				checkTaskJson(createResponse.asJson());
				final WS.Response getResponse = getTask(1L, 1L);
				assertThat(getResponse.getStatus()).isEqualTo(OK);
				checkTaskJson(getResponse.asJson());
			}
		});
	}

	@Test
	public void createdTaskAppearsInListOfTasksForBacklogItem() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Todo App 3.0", 1L).getStatus()).isEqualTo(CREATED);
				assertThat(createBacklogItem("some stuff", 1L).getStatus()).isEqualTo(CREATED);
				assertThat(createTask(1L, "set up project").getStatus()).isEqualTo(CREATED);
				final WS.Response response = getTasks(1L);
				assertThat(response.getStatus()).isEqualTo(OK);
				final JsonNode json = response.asJson();
				assertTrue(json.isArray());
				assertTrue(json.path(0).isObject());
				checkTaskJson(json.path(0));
			}
		});
	}

	private static void checkTaskJson(JsonNode json) {
		assertEquals("id check failed", json.path("id").getIntValue(), 1);
		assertEquals("name check failed", json.path("name").getTextValue(), "set up project");
		assertEquals("description check failed", json.path("description").getTextValue(), "What needs to be done");
		assertEquals("backlogItemId check failed", json.path("backlogItemId").getLongValue(), 1L);
		assertEquals("githubStatus check failed", json.path("githubStatus").getTextValue(), "OPEN");
		assertTrue("githubUrl is not text", json.path("githubUrl").isTextual());
		assertTrue("githubComments is not an array", json.path("githubComments").isArray());
		assertTrue("githubComment must have login parameter", json.path("githubComments").path(0).path("login").isTextual());
		assertTrue("githubComment must have body parameter", json.path("githubComments").path(0).path("body").isTextual());
		assertTrue("githubComment must have url parameter", json.path("githubComments").path(0).path("url").isTextual());
	}

	private static WS.Response changeSummary(Long backlogItemId, String newSummary) {
		final JsonNode body = Json.toJson(ImmutableMap.of("summary", newSummary));
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/summary").put(body).get();
	}

	private static WS.Response prioritize(Long backlogItemId, Priority priority) {
		final JsonNode body = Json.toJson(ImmutableMap.of("priority", priority));
		return WS.url(backlogItemsEndpoint + "/" + backlogItemId + "/priority").put(body).get();
	}

}