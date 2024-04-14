package user;

import config.Rest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserSteps extends Rest {

    @Step("User create")
    public Response create(User user) {
        return getSpec()
                .body(user)
                .post(REGISTER);
    }

    @Step("User change data")
    public Response change(User user, String token) {
        return getSpec()
                .header("Authorization", token)
                .body(user)
                .patch(USER);
    }

    @Step("User authorized")
    public Response login(User user, String token) {
        return getSpec()
                .header("Authorization", token)
                .body(user)
                .post(LOGIN);
    }
    @Step("User delete")
    public void delete(String token) {
        getSpec()
                .header("Authorization", token)
                .delete(USER);
    }
}
