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

import domain.model.User;
import domain.model.UserNameAlreadyTakenException;

public class Users extends Controller {
	
	static Form<User> userForm = Form.form(User.class);
	static Form<User.Profile> profileForm = Form.form(User.Profile.class);

	@BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public static Result register() {
		final JsonNode json = request().body().asJson();
		final Form<User> filled = userForm.bind(json);
		if (filled.hasErrors()) {
			return badRequest(filled.errorsAsJson());
		} else {
			final User user = filled.get();
			try {
				user.add();
			} catch (UserNameAlreadyTakenException ex) {
				return badRequest(Json.toJson(ImmutableMap.of("username", ImmutableList.of(ex.getMessage()))));
			}
			return created();
		}
	}

	public static Result getProfile(String userId) {
		final Optional<User> user = User.forId(userId);
		if (user.isPresent()) {
			return ok(Json.toJson(user.get().profile));
		} else {
		return notFound(Json.toJson(ImmutableMap.of("error", "User with id " + userId + " cannot be found")));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)

	@Transactional
	public static Result updateProfile(String userId) {
		final Optional<User> user = User.forId(userId);
		if (!user.isPresent()) {
			return status(PRECONDITION_FAILED, Json.toJson(ImmutableMap.of("error", "Cannot PUT profile before creating user")));
		} else {
			final JsonNode json = request().body().asJson();
			final Form<User.Profile> filled = profileForm.bind(json);
			if (filled.hasErrors()) {
				return badRequest(filled.errorsAsJson());
			} else {
				final User.Profile profile = filled.get();
				final User theUser = user.get();
				theUser.profile = profile;
				theUser.save();
				return ok();
			}
		}
	}

}