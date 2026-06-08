package PDSAPI.tests.methods;

import PDSAPI.actions.Auth;
import helpers.CreatePolicy;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Тесты для проверки метода сохранения полиса
 */
@Epic("Проверка API методов продукта")
@Feature("Метод сохранения")
public class ImportTests {
    private String sessionToken;

    /**
     * Предустановка с авторизацией
     */
    @BeforeEach
    @Step("Предустановка")
    public void setUp() {
        // Авторизация
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }

    /**
     * Позитивный тест сохранения полиса
     */
    @Test
    @Description("Успешное сохранение полиса")
    public void successImportWithPOJO(){
        // Создание объекта полиса для метода сохранения
        ImportRequest importRequest = ImportRequest.builder()
                .policy(new CreatePolicy().getPolicy())
                .build();

        // Метод сохранения полиса
        ImportResponse response =
                given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(importRequest)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.IMPORT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(ImportResponse.class)
        ;

        // Проверка, что в ответе есть calcID
        assertNotNull(response.getPolicy().getCalcID(), "В ответе нет calcID");

        // Проверка, что страховая премия больше нуля
        assert response.getPolicy().getInsPremTotal() > 0;

        // Проверка того, что нет получаемых ошибок
        try {
            assertNull(response.getWarnings());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getWarnings() != null;
            for (Error error : response.getWarnings().getErrors()) {
                errorMessages.append(error.getDetailMessage()).append("\n");
            }
            throw new AssertionError(errorMessages.toString().trim() + " " + response.getPolicy().getCalcID());
        }

    }

}
