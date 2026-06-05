package PDSAPI.tests;

import PDSAPI.actions.Attach;
import PDSAPI.actions.Auth;
import PDSAPI.actions.CreatePolicy;
import PDSAPI.actions.Import;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IssueTests {
    private String sessionToken, calcID, policyID;
    PolicyImport policy = new CreatePolicy().getPolicy();

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.BASE_URL,
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        ImportResponse response = Import.getImportResponse(ConstantValues.BASE_URL, sessionToken, policy);
        calcID = Import.getCalcID(response);
        policyID = Import.getPolicyID(response);

    }


    @Test
    public void successIssueWithPOJO(){

        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Документ, удостоверяющий личность");
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Анкета для проведения идентификации клиента");
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Согласие на обработку ПД");
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Согласие на доп. услугу");
        IssueRequest request = IssueRequest.builder()
                .policyID(policyID).build();

        IssueResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
//                .log().all()
                .when()
                .post(ConstantValues.ISSUE_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
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

            throw new AssertionError(errorMessages.toString().trim());
        }

    }



}
