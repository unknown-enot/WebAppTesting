package lab3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;


public class PetStore_UserApiTests {
    private final String baseUrl = "https://petstore.swagger.io/v2";
    private final String USER = "/user";
    private final String USER_USERNAME = USER + "/{username}";
    private final String USER_LOGIN = USER + "/login";
    private final String USER_LOGOUT = USER + "/logout";

    private String username;
    private String firstName;

    @BeforeClass
    private void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    private void verifyLoginAction() {
        Map<String, ?> body = Map.of("username", "Grigorii Diachenko", "password", "121м-22з-1");

        Response response = RestAssured.given().body(body).get(USER_LOGIN);

        response.then().statusCode(HttpStatus.SC_OK);

        RestAssured.requestSpecification
                .sessionId(response.jsonPath()
                    .get("message")
                    .toString()
                    .replaceAll("[^0-9]", ""));
    }

    @Test(dependsOnMethods = "verifyLoginAction")
    private void verifyCreateAction() {
        username = Faker.instance().name().username();
        firstName = Faker.instance().harryPotter().character();

        Map<String, ?> body = Map.of(
          "username", username,
          "firstName", firstName,
          "lastName", Faker.instance().gameOfThrones().character(),
          "email", Faker.instance().internet().emailAddress(),
          "password", Faker.instance().internet().password(),
          "phone", Faker.instance().phoneNumber().phoneNumber(),
          "userStatus", Integer.valueOf("1")
        );

        RestAssured.given().body(body)
                .post(USER)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyCreateAction")
    private void verifyGetAction() {
        RestAssured.given().pathParam("username", username)
                .get(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("firstName", equalTo(firstName));
    }

    @Test(dependsOnMethods = "verifyGetAction")
    private void verifyDeleteAction() {
        RestAssured.given().pathParam("username", username)
                .delete(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyLoginAction", priority = 1)
    private void verifyLogoutAction() {
        RestAssured.given()
                .get(USER_LOGOUT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
