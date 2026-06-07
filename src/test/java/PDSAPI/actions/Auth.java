package PDSAPI.actions;

import PDSAPI.models.AuthRequest;
import PDSAPI.models.AuthResponse;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Auth {
    @Step("Авторизация")
    public static String loginUser(String Login, String Password) {
        AuthRequest request = AuthRequest.builder()
                .login(Login)
                .password(Password).build();
        AuthResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.AUTH_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class);

        // Вывод ошибок в Assert при ошибках, получаемых от метода
        try {
            assertNotNull(response.getSessionToken());
        } catch (AssertionError e) {
            throw new AssertionError(response.getMessage());
        }
        
        return response.getSessionToken();
    }
}
