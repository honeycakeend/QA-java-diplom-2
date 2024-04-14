package user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserGenerate {
    public static Faker faker = new Faker();

    @Step("Create new user with random data")
    public static User createUserWithRandomData(){
        return new User(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password()
        );
    }

    @Step("Create new user without name")
    public static User createUserWithoutName(){
        return new User(
                "",
                faker.internet().emailAddress(),
                faker.internet().password()
        );
    }

    @Step("Create new user without email")
    public static User createUserWithoutEmail(){
        return new User(
                faker.name().firstName(),
                "",
                faker.internet().password()
        );
    }

    @Step("Create new user without email")
    public static User createUserWithoutPassword(){
        return new User(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                ""
        );
    }

    @Step("Create new user without data")
    public static User createUserWithoutData(){
        return new User(
                "",
                "",
                ""
        );
    }
}
