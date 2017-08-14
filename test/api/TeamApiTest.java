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
				final WS.Response createResponse = createTeam(TEST_TEAM_NAME);
				assertThat(createResponse.getStatus()).isEqualTo(CREATED);
				JsonNode expectedResponse = Json.parse("{ \"id\": 1, \"name\": \"" + TEST_TEAM_NAME + "\", \"members\": 0 }");
				assertEquals(expectedResponse, createResponse.asJson());
			}
		});
	}
	
	@Test
	public void getTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam(TEST_TEAM_NAME).getStatus()).isEqualTo(CREATED);
				final WS.Response getResponse = getTeam(1L);
				assertThat(getResponse.getStatus()).isEqualTo(OK);
				JsonNode expectedResponse = Json.parse("{ \"id\": 1, \"name\": \""+ TEST_TEAM_NAME + "\", \"members\": 0 }");
				assertEquals(expectedResponse, getResponse.asJson());
			}
		});
	}
	
	@Test
	public void getNonExistingTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				long nonExistingTeamId = TEST_TEAM_NONEXIST_ID;
				assertThat(createTeam(TEST_TEAM_NAME).getStatus()).isEqualTo(CREATED);
				final WS.Response getResponse = getTeam(nonExistingTeamId);
				assertThat(getResponse.getStatus()).isEqualTo(NOT_FOUND);
				JsonNode expectedResponse = Json.parse("{\"error\":\"Team with id " + nonExistingTeamId + " cannot be found\"}");
				assertEquals(expectedResponse, getResponse.asJson());
			}
		});
	}

	@Test
	public void addMemberToTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam(TEST_TEAM_NAME).getStatus()).isEqualTo(CREATED);
				assertThat(createUser(TEST_USER_NAME).getStatus()).isEqualTo(CREATED);
				final WS.Response addMemberToTeamResponse = addMemberToTeam(TEST_TEAM_ID, TEST_USER_NAME);
				assertThat(addMemberToTeamResponse.getStatus()).isEqualTo(CREATED);
				JsonNode expectedResponse = Json.parse("[{ \"identity\" : \"johndoe\", \"email\" : \"john@example.com\", \"name\" : \"John Doe\" }]");
				assertEquals(expectedResponse, addMemberToTeamResponse.asJson());
			}
		});
	}
	
	@Test
	public void membersOfTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createTeam(TEST_TEAM_NAME).getStatus()).isEqualTo(CREATED);
				assertThat(createUser(TEST_USER_NAME).getStatus()).isEqualTo(CREATED);
				assertThat(addMemberToTeam(TEST_TEAM_ID, TEST_USER_NAME).getStatus()).isEqualTo(CREATED);
				final WS.Response response = getMembersOfTeam(TEST_TEAM_ID);
				assertThat(response.getStatus()).isEqualTo(OK);
				JsonNode expectedResponse = Json.parse("[{ \"identity\" : \"johndoe\", \"email\" : \"john@example.com\", \"name\" : \"John Doe\" }]");
				assertEquals(expectedResponse, response.asJson());
			}
		});
	}
	
	@Test
	public void addMemberToNonExistingTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final long nonExistingTeamId = TEST_TEAM_NONEXIST_ID;
				assertThat(createUser(TEST_USER_NAME).getStatus()).isEqualTo(CREATED);
				final WS.Response addMemberToTeamResponse = addMemberToTeam(nonExistingTeamId, TEST_USER_NAME);
				assertThat(addMemberToTeamResponse.getStatus()).isEqualTo(NOT_FOUND);
				JsonNode expectedResponse = Json.parse("{\"error\":\"Team with id " + nonExistingTeamId + " cannot be found\"}");
				assertEquals(expectedResponse, addMemberToTeamResponse.asJson());
			}
		});
	}
	
	@Test
	public void addNonExistingMemberToTeam() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final String nonExistingUserId = TEST_USER_NOTEXIST_NAME;
				assertThat(createTeam(TEST_TEAM_NAME).getStatus()).isEqualTo(CREATED);
				final WS.Response addMemberToTeamResponse = addMemberToTeam(TEST_TEAM_ID, nonExistingUserId);
				assertThat(addMemberToTeamResponse.getStatus()).isEqualTo(NOT_FOUND);
				JsonNode expectedResponse = Json.parse("{\"error\":\"User with id " + nonExistingUserId + " cannot be found\"}");
				assertEquals(expectedResponse, addMemberToTeamResponse.asJson());
			}
		});
	}
	
}