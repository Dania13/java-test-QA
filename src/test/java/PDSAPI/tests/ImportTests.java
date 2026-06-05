package PDSAPI.tests;

import PDSAPI.actions.Auth;
import helpers.CreatePolicy;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ImportTests {
    private String sessionToken;

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }

    @Test
    public void successImportWithPOJO(){

        ImportRequest importRequest = new ImportRequest(new CreatePolicy().getPolicy());

        ImportResponse response =
                given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(importRequest)
//                .log().all()
                .when()
                .post(ConstantValues.IMPORT_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(ImportResponse.class)
        ;

        assertNotNull(response.getPolicy().getCalcID());
        assert response.getPolicy().getInsPremTotal() > 0;
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
