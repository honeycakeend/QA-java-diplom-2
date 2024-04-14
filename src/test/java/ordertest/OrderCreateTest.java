package ordertest;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderSteps;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserGenerate;
import user.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private OrderSteps orderSteps = new OrderSteps();
    private final Faker faker = new Faker();

    @Before
    public void setUp(){
        User user = UserGenerate.createUserWithRandomData();
        Response response = userSteps.create(user);
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
    @DisplayName("Create order valid")
    public void orderValid() {
        orderSteps = new OrderSteps();
        Response getIngredient = orderSteps.getIngredients();
        List<String> ingredients = new ArrayList<>(getIngredient
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("data._id"));
        Order order = new Order(ingredients.subList(3,4));

        orderSteps.createOrderWithAuthorized(order, accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order with token and without ingredients")
    public void orderWithTokenWithoutIngredients() {
        orderSteps = new OrderSteps();
        Order order = new Order(null);
        orderSteps.createOrderWithAuthorized(order, accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with token and invalid hash code")
    public void orderWithIncorrectHash() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().password());
        Order order = new Order(ingredients);
        orderSteps = new OrderSteps();
        orderSteps.createOrderWithAuthorized(order, accessToken)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Create order without token")
    public void orderWithoutToken() {
        orderSteps = new OrderSteps();
        Response getIngredient = orderSteps.getIngredients();
        List<String> ingredients = new ArrayList<>(getIngredient
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("data._id"));
        Order order = new Order(ingredients.subList(4,5));

        orderSteps.createOrderWithoutAuthorized(order)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

}
