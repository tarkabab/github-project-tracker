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

public class TeamApiTest extends AbstractApiTest {

	@Test
	public void canCreateTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final WS.Response createResponse = createTeam("Team Foo");
				assertThat(createResponse.getStatus()).isEqualTo(CREATED);
				checkJsonBody(createResponse.asJson());
			}
		});
	}
	
	private static void checkJsonBody(JsonNode json) {
		checkJsonBody(json, "Team Foo");
	}

	private static void checkJsonBody(JsonNode json, String teamName) {
		assertThat(json.path("id").getLongValue()).isEqualTo(1L);
		assertThat(json.path("name").getTextValue()).isEqualTo(teamName);
	}
}