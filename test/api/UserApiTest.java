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
	public void canGetACreatedUser() {
		running(testServer(3333), new Runnable() {
			public void run() {
				assertThat(createUser("johndoe").getStatus()).isEqualTo(CREATED);
				WS.Response resp = getUserProfile("johndoe");
				assertThat(resp.getStatus()).isEqualTo(OK);
				assertEquals(resp.asJson(), Json.toJson(ImmutableMap.of(
					"email", "john@example.com",
					"firstName", "John",
					"lastName", "Doe",
					"age", 30
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