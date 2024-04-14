package usertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserGenerate;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static user.UserGenerate.faker;

public class UserChangeDataTest {

    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerate.createUserWithRandomData();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
    }

    @After
    public void downUp() {
        if (accessToken != null) userSteps.delete(accessToken);
    }

    @Test
    @DisplayName("User change data")
    public void changeAuthorizedUpdateUser() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.change(user, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("User change password")
    public void changeAuthorizedUserPassword() {
        user.setPassword(faker.internet().password());
        response = userSteps.change(user, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Unauthorized user change password")
    public void changeUserDataPasswordWithoutAuthorized() {
        user.setPassword(faker.internet().password());
        response = userSteps.change(user, "");
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Unauthorized user change data")
    public void changeUnauthorizedUser() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.change(user, "");
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }
}
