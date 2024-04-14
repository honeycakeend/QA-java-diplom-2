package usertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserGenerate;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @After
    public void downUp() {
        if (accessToken != null) userSteps.delete(accessToken);
    }

    @Test
    @DisplayName("User create with valid data")
    @Description("User create with valid data")
    public void createUserValid() {
        user = UserGenerate.createUserWithRandomData();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Create a user with existing data")
    @Description("Create user")
    public void createUserWithExistingData() {
        user = UserGenerate.createUserWithRandomData();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
        response.then()
                .statusCode(HttpStatus.SC_OK);
        response = userSteps.create(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create a user without completed data")
    public void createUserCreateUserWithoutData() {
        user = UserGenerate.createUserWithoutData();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Creating a user without email")
    public void createUserWithoutEmail() {
        user = UserGenerate.createUserWithoutEmail();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create a user without password")
    public void createUserWithoutPassword() {
        user = UserGenerate.createUserWithoutPassword();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create a user without name")
    public void createUserWithoutName() {
        user = UserGenerate.createUserWithoutName();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
