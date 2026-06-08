package PDSAPI.tests;

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

@Epic("Проверка API методов продукта")
@Feature("Метод сохранения")
public class ImportTests {
    private String sessionToken;

    @BeforeEach
    @Step("Предустановка")
    public void setUp() {
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }

    @Test
    @Description("Успешное сохранение полиса")
    public void successImportWithPOJO(){

        ImportRequest importRequest = ImportRequest.builder()
                .policy(new CreatePolicy().getPolicy())
                .build();

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

        assertNotNull(response.getPolicy().getCalcID());
        assert response.getPolicy().getInsPremTotal() > 0;

        // Вывод ошибок в Assert при ошибках, получаемых от метода
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
