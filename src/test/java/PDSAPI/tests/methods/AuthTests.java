package PDSAPI.tests.methods;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
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
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Тесты для проверки метода авторизации
 */
@Epic("Проверка API методов продукта")
@Feature("Метод Авторизация")
public class AuthTests {

    /**
     * Тест для успешной авторизации, на вход подаётся JSON
     */
    @Test
    @Description("Успешная авторизация")
    public void successAuth(){
        String requestBody = """
                {
                    "login": "%s",
                    "password": "%s"
                }
                """.formatted(ConstantValues.LOGIN_AUTH, ConstantValues.PASSWORD_AUTH);
        given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.AUTH_ENDPOINT)
        .then()
                .statusCode(200) // Проверяем статус код
                .body("success", equalTo(true)) // Проверяем значение поля в JSON
                ;
    }

    /**
     * Тест для неуспешной авторизации, на вход подаётся JSON
     */
    @Description("Авторизация с неверным паролем")
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
                .body(requestBody)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.AUTH_ENDPOINT)
        .then()
                .statusCode(200) // Проверяем статус код
                .body("success", equalTo(false)) // Проверяем значение поля в JSON
               ;
    }


    /**
     * Параметрищированный тест, на вход подаётся разные варианты пароля,
     * а так же результат успешности теста.
     *
     * @param password пароль
     * @param success ожидаемый результат теста
     */
    @ParameterizedTest
    @MethodSource("provideAuthData")
    @Description("Проверка метода с передачей разных параметров на вход")
    public void successAuthWithPOJO(String password, boolean success){
        AuthRequest user = AuthRequest.builder()
                .login(ConstantValues.LOGIN_AUTH)
                .password(password).build();
        AuthResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(user)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.AUTH_ENDPOINT)
        .then()
                .statusCode(200) // Проверяем статус код
                .extract()
                .as(AuthResponse.class);


        // Проверка, что в ответе пришёл success, котррый передали на вход теста
        assertEquals(success, response.getSuccess());

        if (success) {
            // Проверка, что если авторизация успешна, то SessionToken имеет определённую длину
            assertEquals(36, response.getSessionToken().length());

            // Проверка, что если авторизация успешна, то нет поля Message
            assertNull(response.getMessage());
        } else {

            // Проверка, что если авторизация неуспешна, то есть поле Message
            assertNotNull(response.getMessage());
        }
    }

    // Метод куда передаются варианты комбинаций пароля и результата теста
    static Stream<Arguments> provideAuthData() {
        String validPassword = ConstantValues.PASSWORD_AUTH;
        return Stream.of(
                Arguments.of(validPassword, true)
                ,Arguments.of(validPassword+"1", false)
                ,Arguments.of(null, false)
        );
    }
}
