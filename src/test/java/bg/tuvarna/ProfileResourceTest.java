package bg.tuvarna;

import bg.tuvarna.enums.EmployeePosition;
import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.models.dto.requests.LoginDTO;
import bg.tuvarna.testResourcesManager.TestContainersResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(TestContainersResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileResourceTest {

    @Test
    @Order(1)
    void testUNAUTHORIZEDRegister() {
        CreateUserDTO userDto = new CreateUserDTO("username1", "password", EmployeePosition.MOL);
        given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when().post("/inventory-api/v1/profile/register")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(2)
    void testInvalidLogin() {
        LoginDTO userDto = new LoginDTO("username1", "password");
        given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when().post("/inventory-api/v1/profile/login")
                .then()
                .statusCode(401)
                .body("message", equalTo("Invalid credentials"));
    }

    @Test
    @Order(3)
    void testValidLogin() {
        LoginDTO userDto = new LoginDTO("admin", "admin");
        given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when().post("/inventory-api/v1/profile/login")
                .then()
                .statusCode(200)
                .header("Authorization", notNullValue());
    }

    @Test
    @Order(4)
    void testValidRegister() {
        LoginDTO loginDTO = new LoginDTO("admin", "admin");
        String token = given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when().post("/inventory-api/v1/profile/login")
                .then()
                .statusCode(200)
                .header("Authorization", notNullValue())
                .extract()
                .header("Authorization");

        CreateUserDTO userDto = new CreateUserDTO("test", "test", EmployeePosition.WORKER);
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(userDto)
                .when().post("/inventory-api/v1/profile/register")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(5)
    void testForbiddenRegister() {
        LoginDTO loginDTO = new LoginDTO("test", "test");
        String token = given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when().post("/inventory-api/v1/profile/login")
                .then()
                .statusCode(200)
                .header("Authorization", notNullValue())
                .extract()
                .header("Authorization");

        CreateUserDTO userDto = new CreateUserDTO("test2", "tes2", EmployeePosition.WORKER);
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(userDto)
                .when().post("/inventory-api/v1/profile/register")
                .then()
                .statusCode(403);
    }

    @Test
    @Order(6)
    void testGetProfile() {
        LoginDTO loginDTO = new LoginDTO("test", "test");
        String token = given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when().post("/inventory-api/v1/profile/login")
                .then()
                .statusCode(200)
                .header("Authorization", notNullValue())
                .extract()
                .header("Authorization");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/inventory-api/v1/profile")
                .then()
                .statusCode(200)
                .body("username", equalTo("test"))
                .body("roles", equalTo(List.of("WORKER", "CLIENT")));
    }
}
