package ordertest;

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

public class OrderGetFromUserTest {

    private final UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();
    private String accessToken;

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
    @DisplayName("Get user order")
    public void getOrder() {
        orderSteps = new OrderSteps();
        Response getIngredient = orderSteps.getIngredients();
        List<String> ingredients = new ArrayList<>(getIngredient
                .then()
                .extract()
                .path("data._id"));
        Order order = new Order(ingredients.subList(0,1));
        orderSteps.createOrderWithAuthorized(order, accessToken);
        orderSteps.getUserOrders(accessToken)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Get user order without token")
    public void getOrderWithoutToken() {
        orderSteps.getUserOrderWithoutToken()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
