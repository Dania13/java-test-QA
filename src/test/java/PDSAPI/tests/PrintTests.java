package PDSAPI.tests;

import PDSAPI.actions.*;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import helpers.CreatePolicy;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Epic("Проверка API методов продукта")
@Feature("Метод Печати полиса")
public class PrintTests {
    private String sessionToken, calcID, policyID, number;

    @BeforeEach
    @Step("Предустановка")
    public void setUp() {
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        ImportResponse response = Import.importPolicy(sessionToken, new CreatePolicy().getPolicy());
        calcID = Import.getCalcId(response);
        policyID = Import.getPolicyId(response);
        number = Import.getNumber(response);
    }


    @Test
    @Description("Печать оформленного полиса")
    public void successPrintWithPOJO(){
        Attach.AttachDocs(sessionToken, calcID, "Документ, удостоверяющий личность");
        Attach.AttachDocs(sessionToken, calcID, "Анкета для проведения идентификации клиента");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на обработку ПД");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на доп. услугу");

        Issue.issuePolicy(sessionToken, policyID);

        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Печать").build();

        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.PRINT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(PrintResponse.class)
        ;

        assertEquals(number, response.getNumber());

        // Вывод ошибок в Assert при ошибках, получаемых от метода
        try {
            assertNull(response.getErrors());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getErrors() != null;
            for (Error error : response.getErrors().getErrors()) {
                errorMessages.append(error.getMessage()).append("\n");
            }

            throw new AssertionError(errorMessages.toString().trim() + " " + calcID);
        }

    }

    @Test
    @Description("Печать Проекта")
    public void successPrintDraftPOJO(){

        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Черновик").build();

        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.PRINT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(PrintResponse.class)
                ;

        assertEquals(number, response.getNumber());

        // Вывод ошибок в Assert при ошибках, получаемых от метода
        try {
            assertNull(response.getErrors());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getErrors() != null;
            for (Error error : response.getErrors().getErrors()) {
                errorMessages.append(error.getMessage()).append("\n");
            }

            throw new AssertionError(errorMessages.toString().trim() + " " + calcID);
        }
    }
}
