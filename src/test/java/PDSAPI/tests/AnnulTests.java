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
import static org.junit.jupiter.api.Assertions.*;

public class AnnulTests {
    private String sessionToken, calcID, policyID;
    PolicyImport policy = new CreatePolicy().getPolicy();

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        ImportResponse response = Import.getImportResponse(sessionToken, policy);
        calcID = Import.getCalcID(response);
        policyID = Import.getPolicyID(response);
    }


    @Test
    public void successAnnulWithPOJO(){
        Attach.AttachDocs(sessionToken, calcID, "Документ, удостоверяющий личность");
        Attach.AttachDocs(sessionToken, calcID, "Анкета для проведения идентификации клиента");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на обработку ПД");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на доп. услугу");

        Issue.IssuePolicy(sessionToken, policyID);

        AnnulRequest request = AnnulRequest.builder()
                .calcID(calcID)
                .reason("Тест").build();

        AnnulResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
//                .log().all()
                .when()
                .post(ConstantValues.ANNUL_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(AnnulResponse.class)
                ;

        assertEquals(sessionToken, response.getAccID());
        assertEquals(calcID, response.getCalcID());
        try {
            assertTrue(response.isOk());
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
    public void badAnnulDraftPOJO(){

        AnnulRequest request = AnnulRequest.builder()
                .calcID(calcID)
                .reason("Тест").build();

        AnnulResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
//                .log().all()
                .when()
                .post(ConstantValues.ANNUL_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(AnnulResponse.class)
                ;

        assertEquals(sessionToken, response.getAccID());
        assertEquals(calcID, response.getCalcID());
        assertFalse(response.isOk());
        assertNotNull(response.getErrors());

    }

}
