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

public class UserApiTest extends AbstractApiTest {

	@Test
	public void canCreateUser() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createUser(TEST_USER_NAME).getStatus()).isEqualTo(CREATED);
			}
		});
	}

	@Test
	public void cannotCreateDuplicateUser() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final String username = TEST_USER_NAME; 
				assertThat(createUser(username).getStatus()).isEqualTo(CREATED);
				WS.Response resp = createUser(username);
				assertThat(resp.getStatus()).isEqualTo(BAD_REQUEST);
				JsonNode expectedResponse = Json.parse("{\"username\":[\"User name " + username + " is already taken\"]}");
				assertEquals(expectedResponse, resp.asJson());
			}
		});
	}

	@Test
	public void emailFieldIsMandatory() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final String username = TEST_USER_NAME; 
				final String email = ""; 
				final JsonNode userRequest = createUserRequest(username, email);
				WS.Response resp = createUser(userRequest);
				assertThat(resp.getStatus()).isEqualTo(BAD_REQUEST);
				// TODO: deviation from requirement: {"email":["This field is required"])}, needs to be clarified
				JsonNode expectedResponse = Json.parse("{\"profile.email\":[\"This field is required\"]}");
				assertEquals(expectedResponse, resp.asJson());
			}
		});
	}

	@Test
	public void canGetUserProfie() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final String username = TEST_USER_NAME;
				assertThat(createUser(username).getStatus()).isEqualTo(CREATED);
				WS.Response resp = getUserProfile(username);
				assertThat(resp.getStatus()).isEqualTo(OK);
				JsonNode expectedResponse = Json.toJson(ImmutableMap.of(
					"email", TEST_USER_EMAIL,
					"firstName", TEST_USER_FIRST_NAME,
					"lastName", TEST_USER_LAST_NAME,
					"age", TEST_USER_AGE
					));
				assertEquals(expectedResponse, resp.asJson());
			}
		});
	}

	@Test
	public void profileOfNonExistingUser() {
		running(testServer(3333), new Runnable()  {
			public void run() {
				final String username = TEST_USER_NOTEXIST_NAME;
				WS.Response resp = getUserProfile(username);
				assertThat(resp.getStatus()).isEqualTo(NOT_FOUND);
				JsonNode expectedResponse = Json.parse("{\"error\":\"User with id " + username + " cannot be found\"}");
				assertEquals(expectedResponse, resp.asJson());
			}
		});
	}

	@Test
	public void canUpdateAUserProfile() {
		running(testServer(3333), new Runnable()  {
			public void run() {
				final String username = TEST_USER_NAME;
				final String updatedEmail = TEST_USER_UPDATED_EMAIL;
				assertThat(createUser(username).getStatus()).isEqualTo(CREATED);
				assertThat(updateProfile(username, updatedEmail).getStatus()).isEqualTo(OK);
				assertThat(getUserProfile(username).asJson().path("email").getTextValue())
					.isEqualTo(updatedEmail);
			}
		});
	}

	@Test
	public void updateProfileOfNonExistingUser() {
		running(testServer(3333), new Runnable()  {
			public void run() {
				final String username = TEST_USER_NOTEXIST_NAME;
				final String updatedEmail = TEST_USER_UPDATED_EMAIL;
				WS.Response resp = updateProfile(username, updatedEmail);
				assertThat(resp.getStatus()).isEqualTo(PRECONDITION_FAILED);
				JsonNode expectedResponse = Json.parse("{\"error\":\"Cannot PUT profile before creating user\"}");
				System.out.println(resp.asJson());
				assertEquals(expectedResponse, resp.asJson());
			}
		});
	}

}