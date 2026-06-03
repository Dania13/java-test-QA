package PDSAPI.tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import PDSAPI.models.AuthRequest;
import PDSAPI.models.AuthResponse;
import PDSAPI.specs.ConstantValues;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class AuthTests {

    @Test
    public void successAuth(){
        String requestBody = """
                {
                    "login": "AutoQA",
                    "password": "AutoQA"
                }
                """;
        given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody).
        when()
                .post(ConstantValues.AUTH_ENDPOINT).
        then()
                .statusCode(200) // Проверяем статус код
                .body("success", equalTo(true)) // Проверяем значение поля в JSON
                .log().all();
    }

    @Test
    public void badAuth(){
        String requestBody = String.format("""
                {
                    "login": "%s",
                    "password": "%s1"
                }
                """, ConstantValues.LOGIN_AUTH, ConstantValues.LOGIN_AUTH);
        given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody).
        when()
                .post(ConstantValues.AUTH_ENDPOINT).
        then()
                .statusCode(200) // Проверяем статус код
                .body("success", equalTo(false)) // Проверяем значение поля в JSON
                .log().all();
    }

    @ParameterizedTest
    @MethodSource("provideAuthData")
    public void successAuthWithPOJO(String password, boolean success){
        AuthRequest user = AuthRequest.builder()
                .login(ConstantValues.LOGIN_AUTH)
                .password(password).build();
        AuthResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(user).
        when()
                .post(ConstantValues.AUTH_ENDPOINT).
        then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(AuthResponse.class);


        assertEquals(success, response.getSuccess());
        if (success) {
            assertEquals(36, response.getSessionToken().length());
            assertNull(response.getMessage());
        } else {
            assertNotNull(response.getMessage());
        }
    }

    static Stream<Arguments> provideAuthData() {
        String validPassword = ConstantValues.PASSWORD_AUTH;
        return Stream.of(
                Arguments.of(validPassword, true),
                Arguments.of(validPassword+"1", false),
                Arguments.of(null, false)
        );
    }
}
