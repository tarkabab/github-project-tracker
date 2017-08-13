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
	public void cannotCreateDuplicateUser() {
		running(testServer(3333), new Runnable() {
			public void run() {
				final String username = TEST_USER_NAME; 
				assertThat(createUser(username).getStatus()).isEqualTo(CREATED);
				WS.Response resp = createUser(username);
				assertThat(resp.getStatus()).isEqualTo(BAD_REQUEST);
				assertEquals(resp.asJson(), Json.parse("{\"username\":[\"User name " + username + " is already taken\"]}"));
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
				assertEquals(resp.asJson(), Json.parse("{\"profile.email\":[\"This field is required\"]}"));
			}
		});
	}

	@Test
	public void canGetACreatedUser() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createUser("johndoe").getStatus()).isEqualTo(CREATED);
				WS.Response resp = getUserProfile("johndoe");
				System.out.println(resp.getHeader("Location"));
				assertThat(resp.getStatus()).isEqualTo(OK);
				assertEquals(resp.asJson(), Json.toJson(ImmutableMap.of(
					"email", "john@example.com",
					"firstName", "John",
					"lastName", "Doe",
					"age", 32
					)));
			}
		});
	}

	@Test
	public void canUpdateAUserProfile() {
		running(testServer(3333), new Runnable()  {
			public void run() {
				assertThat(createUser("johndoe").getStatus()).isEqualTo(CREATED);
				assertThat(updateProfile("johndoe", "johnny@hotmail.com").getStatus()).isEqualTo(OK);
				assertThat(getUserProfile("johndoe").asJson().path("email").getTextValue())
					.isEqualTo("johnny@hotmail.com");
			}
		});
	}

}