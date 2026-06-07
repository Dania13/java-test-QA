package PDSAPI.actions;

import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Import {
    @Step("Сохранение полиса с расчётом")
    public static ImportResponse ImportPolicy (String sessionToken, PolicyImport policy) {
        ImportRequest request = ImportRequest.builder()
                .policy(policy).build();
        ImportResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .body(request)
        .when()
                .post(ConstantValues.IMPORT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(ImportResponse.class);

        // Вывод ошибок в Assert при ошибках, получаемых от метода
        try {
            assertNull(response.getWarnings());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getWarnings() != null;
            for (Error error : response.getWarnings().getErrors()) {
                errorMessages.append(error.getDetailMessage()).append("\n");
            }

            throw new AssertionError(errorMessages.toString().trim()+ " " + response.getPolicy().getCalcID());
        }
        return response;
    }

    @Step("Расчёт и сохранение полиса")
    public static ImportResponse getImportResponse(String sessionToken, PolicyImport policy) {
        return ImportPolicy(sessionToken, policy);
    }

    // Метод получения CalcID из ответа
    @Step("Получения CalcID ({response.policy.calcID})")
    public static String getCalcID(ImportResponse response) {
        Allure.addAttachment("CalcID", "text/plain", response.getPolicy().getCalcID(), "txt");
        return response.getPolicy().getCalcID();
    }
    // Метод получения PolicyID из ответа
    @Step("Получения PolicyID ({response.policy.ID})")
    public static String getPolicyID(ImportResponse response) {
        return response.getPolicy().getID();
    }
    // Метод получения номера полиса из ответа

    @Step("Получения номера полиса ({response.policy.number})")
    public static String getNumber(ImportResponse response) {
        return response.getPolicy().getNumber();
    }
}
