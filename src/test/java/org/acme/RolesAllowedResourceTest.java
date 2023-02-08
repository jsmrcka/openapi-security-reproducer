package org.acme;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@QuarkusTest
public class RolesAllowedResourceTest {
    @Test
    public void rolesAllowedUser() {
        given()
                .auth().none()
                .get("/roles-allowed-user")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
        given()
                .auth().basic("user", "user")
                .get("/roles-allowed-user")
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
        given()
                .auth().basic("admin", "admin")
                .get("/roles-allowed-user")
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void rolesAllowedAdmin() {
        given()
                .auth().none()
                .get("/roles-allowed-user/roles-allowed-admin")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
        given()
                .auth().basic("user", "user")
                .get("/roles-allowed-user/roles-allowed-admin")
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
        given()
                .auth().basic("admin", "admin")
                .get("/roles-allowed-user/roles-allowed-admin")
                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void openApi() {
        final Response response = given()
                .accept(ContentType.JSON)
                .get("/q/openapi");

        response.then().statusCode(HttpStatus.SC_OK);
        final JsonPath json = response.getBody().jsonPath();

        assertEquals("user", json.getString("paths.'/roles-allowed-user'.get.security[0].SecurityScheme[0]"));

        // This fails on Quarkus 999-SNAPSHOT
        assertEquals("admin",
                json.getString("paths.'/roles-allowed-user/roles-allowed-admin'.get.security[0].SecurityScheme[0]"));
    }
}
