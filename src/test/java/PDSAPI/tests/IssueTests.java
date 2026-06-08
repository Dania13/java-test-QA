package PDSAPI.tests;

import PDSAPI.actions.Attach;
import PDSAPI.actions.Auth;
import helpers.CreatePolicy;
import PDSAPI.actions.Import;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Epic("Проверка API методов продукта")
@Feature("Метод оформления")
public class IssueTests {
    private String sessionToken, calcID, policyID;

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

    }


    @Test
    @Description("Оформление полиса в статусе Проект")
    public void successIssueWithPOJO(){

        Attach.AttachDocs(sessionToken, calcID, "Документ, удостоверяющий личность");
        Attach.AttachDocs(sessionToken, calcID, "Анкета для проведения идентификации клиента");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на обработку ПД");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на доп. услугу");
        IssueRequest request = IssueRequest.builder()
                .policyID(policyID).build();

        IssueResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.ISSUE_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(IssueResponse.class)
        ;

        assertEquals(sessionToken, response.getAccID());
        try {
            assertNull(response.getErrors());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getErrors() != null;
            for (Error error : response.getErrors().getErrors()) {
                errorMessages.append(error.getMessage()).append("\n");
            }

            throw new AssertionError(errorMessages.toString().trim()+ " " + calcID);
        }
    }
}
