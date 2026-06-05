package PDSAPI.actions;

import PDSAPI.models.AuthRequest;
import PDSAPI.models.AuthResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Auth {
    public static String loginUser(String BaseUrl, String Endpoint, String Login, String Password) {
        AuthRequest user = AuthRequest.builder()
                .login(Login)
                .password(Password).build();
        AuthResponse response = given()
                .baseUri(BaseUrl)
                .contentType(ContentType.JSON)
                .body(user).
                when()
                .post(Endpoint).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(AuthResponse.class);

        try {
            assertNotNull(response.getSessionToken());
        } catch (AssertionError e) {
            throw new AssertionError(response.getMessage());
        }
        
        return response.getSessionToken();
    }
}
