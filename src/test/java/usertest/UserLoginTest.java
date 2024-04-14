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

public class UserLoginTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @Before
    public void setUp(){
        user = UserGenerate.createUserWithRandomData();
        response = userSteps.create(user);
        accessToken = response
                .then()
                .extract()
                .body()
                .path("accessToken");
    }

    @After
    public void downSet(){
        if (accessToken != null) userSteps.delete(accessToken);
    }

    @Test
    @DisplayName("Authorization with valid data")
    public void loginUserWithValidData(){
        response = userSteps.login(user, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Authorization with invalid data")
    public void loginUserWithInvalidData(){
        String email = user.getEmail();
        user.setEmail("invalid@gmail.com");
        String password = user.getPassword();
        user.setPassword("invalidpassword");
        response = userSteps.login(user, accessToken);
        user.setEmail(email);
        user.setPassword(password);
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }
}
