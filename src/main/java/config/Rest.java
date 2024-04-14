package config;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Rest {
    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    protected static final String INGREDIENTS = "api/ingredients";
    protected static final String ORDERS = "api/orders";
    protected static final String REGISTER = "api/auth/register";
    protected static final String LOGIN = "api/auth/login";
    protected static final String USER = "api/auth/user";

    public static RequestSpecification getSpec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }
}
