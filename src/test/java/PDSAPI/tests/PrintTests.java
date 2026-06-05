package PDSAPI.tests;

import PDSAPI.actions.*;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import helpers.CreatePolicy;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrintTests {
    private String sessionToken, calcID, policyID, number;
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
        number = Import.getNumber(response);
    }


    @Test
    public void successPrintWithPOJO(){
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Документ, удостоверяющий личность");
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Анкета для проведения идентификации клиента");
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Согласие на обработку ПД");
        Attach.AttachDocs(ConstantValues.BASE_URL, sessionToken, calcID, "Согласие на доп. услугу");

        Issue.IssuePolicy(ConstantValues.BASE_URL, sessionToken, policyID);

        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Печать").build();

        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
//                .log().all()
                .when()
                .post(ConstantValues.PRINT_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(PrintResponse.class)
        ;

        assertEquals(number, response.getNumber());
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

    @Test
    public void successPrintDraftPOJO(){

        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Черновик").build();

        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
//                .log().all()
                .when()
                .post(ConstantValues.PRINT_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(PrintResponse.class)
                ;

        assertEquals(number, response.getNumber());
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
