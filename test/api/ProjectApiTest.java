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

public class ProjectApiTest extends AbstractApiTest {

	@Test
	public void canGetACreatedProject() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				final WS.Response createResponse = createProject("Yet Another To-Do App", 1L);
				assertThat(createResponse.getStatus()).isEqualTo(CREATED);
				checkJsonBody(createResponse.asJson());
				final WS.Response getResponse = getProject(1L);
				assertThat(getResponse.getStatus()).isEqualTo(OK);
				checkJsonBody(getResponse.asJson());
			}
		});
	}

	@Test
	public void anUpdatedDescriptionIsRepresentedInTheResource() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam("Team Foo").getStatus()).isEqualTo(CREATED);
				assertThat(createProject("Yet Another To-Do App", 1L)).isEqualTo(CREATED);
				final WS.Response updateResponse = changeDescription(1L, "new description");
				assertThat(updateResponse.getStatus()).isEqualTo(OK);
				final WS.Response getResponse = getProject(1L);
				assertThat(getResponse.getStatus()).isEqualTo(OK);
				checkJsonBody(getResponse.asJson());
			}
		});
	}

	private static void checkJsonBody(JsonNode json) {
		checkJsonBody(json, "The best project in the world");
	}
	private static void checkJsonBody(JsonNode json, String description) {
		assertThat(json.path("id").getLongValue()).isEqualTo(1L);
		assertThat(json.path("name").getTextValue()).isEqualTo("Yet Another To-Do App");
		assertThat(json.path("description").getTextValue()).isEqualTo(description);
		assertThat(json.path("teamId").getLongValue()).isEqualTo(1L);
		assertTrue(json.path("githubUrl").isTextual());
		assertTrue(json.path("gitUrl").isTextual());
		assertTrue(json.path("githubWatchers").isInt());
		assertTrue(json.path("githubForks").isInt());
	}

	private static WS.Response changeDescription(Long projectId, String newDescription) {
		final JsonNode json = Json.toJson(ImmutableMap.of("description", newDescription));
		return WS.url(projectsEndpoint + "/" + projectId + "/description").put(json).get();
	}

}