package lab4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MockServer_ApiTests {
    private final String ID = "40b792cb-3d26-48be-afa0-b0049ccfc0f3";
    private final String BASE_URL = "https://" + ID + ".mock.pstmn.io";
    private final String PRODUCTS = "/api/products";
    private final String PROFILE_URL = "https://api.getpostman.com/mocks/" + ID;

    @BeforeClass
    private void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    private void verifyGetAllProductsGetAction() {
        Response response = RestAssured.given().get(PRODUCTS);

        response.then().statusCode(HttpStatus.SC_OK);

        Assert.assertNotEquals(response.jsonPath().getList("$").size(), 0);
    }

    @Test
    private void verifyGetProductByIdGetAction() {
        int productId = 1;
        Response response = RestAssured.given().get(PRODUCTS + "/" + productId);

        response.then().statusCode(HttpStatus.SC_OK);
        response.then().body("id", Matchers.equalTo(productId));
    }

    @Test
    private void verifyCreateProductPostAction() {
        Response response = RestAssured.given().post(PRODUCTS);

        response.then().statusCode(HttpStatus.SC_CREATED);
        response.then().body("$", Matchers.hasKey("message"));
    }

    @Test
    private void verifyUpdateProductPutAction() {
        int productId = 1;
        Response response = RestAssured.given().put(PRODUCTS + "/" + productId);

        response.then().statusCode(HttpStatus.SC_OK);
        response.then().body("$", Matchers.hasKey("message"));
    }

    @Test
    private void verifyDeleteProductDeleteAction() {
        int productId = 1;
        Response response = RestAssured.given().delete(PRODUCTS + "/" + productId);

        response.then().statusCode(HttpStatus.SC_OK);
        response.then().body("$", Matchers.hasKey("message"));
    }

    @Test
    private void verifyRetrieveProfileGetAction_ifApiKeySpecified() {
        RestAssured.baseURI = PROFILE_URL;

        Response response = RestAssured.given()
                .header("x-api-key", "PMAK-63ac8872f2404125d942d2a1-820102b141b706325f252244861fa99ea9")
                .get(PROFILE_URL);

        response.then().statusCode(HttpStatus.SC_OK);
        response.then().body("mock.id", Matchers.equalTo(ID));
    }

    @Test
    private void verifyRetrieveProfileGetAction_ifApiKeyNotSpecified() {
        RestAssured.baseURI = PROFILE_URL;

        Response response = RestAssured.given().get(PROFILE_URL);

        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
