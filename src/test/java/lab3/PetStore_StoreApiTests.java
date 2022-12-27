package lab3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class PetStore_StoreApiTests {
    private final String BASE_URL = "https://petstore.swagger.io/v2";
    private final String STORE = "/store";
    private final String STORE_INVENTORY = STORE + "/inventory";
    private final String STORE_ORDER = STORE + "/order";

    private long orderId;

    @BeforeClass
    private void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    private void verifyInventoriesByStatusGetAction() {
        RestAssured.given()
                .get(STORE_INVENTORY)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    private void verifyPlaceOrderPostAction() {
        String orderStatus = "placed by Diachenko G. from 121м-22з-1";
        Map<String, ?> body = Map.of(
                "petId", 0,
                "quantity", 0,
                "shipDate", "2022-12-26",
                "status", orderStatus);

        Response response = RestAssured.given().body(body).post(STORE_ORDER);

        response.then().statusCode(HttpStatus.SC_OK);

        response.then().body("status", Matchers.equalTo(orderStatus));

        response.then().body("id", Matchers.not(Matchers.equalTo(0)));
        this.orderId = response.then().extract().path("id");
    }

    @Test(dependsOnMethods = "verifyPlaceOrderPostAction")
    private void verifyFindOrderByIdGetAction() {
        RestAssured.given()
                .get(STORE_ORDER + "/" + this.orderId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = { "verifyPlaceOrderPostAction", "verifyFindOrderByIdGetAction" })
    private void verifyDeleteOrderByIdDeleteAction() {
        RestAssured.given()
                .delete(STORE_ORDER + "/" + this.orderId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
