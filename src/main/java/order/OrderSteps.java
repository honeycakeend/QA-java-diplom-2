package order;

import config.Rest;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderSteps extends Rest {

    @Step("Create order with token")
    public Response createOrderWithAuthorized(Order order, String accessToken) {
        return getSpec()
                .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Get ingredients")
    public Response getIngredients() {
        return getSpec()
                .get(INGREDIENTS);
    }

    @Step("Create order without token")
    public Response createOrderWithoutAuthorized(Order order) {
        return getSpec()
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Get orders user with token")
    public Response getUserOrders(String accessToken) {
        return getSpec()
                .headers("Authorization", accessToken)
                .when()
                .get(ORDERS);
    }
    @Step("Get orders user without token")
    public Response getUserOrderWithoutToken() {
        return getSpec()
                .when()
                .get(ORDERS);
    }
}
