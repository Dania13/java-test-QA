package PDSAPI.tests;

import PDSAPI.actions.Auth;
import PDSAPI.actions.CreatePolicy;
import PDSAPI.actions.Import;
import PDSAPI.models.*;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class AttachTests {
    private String sessionToken, calcID;
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
    }


    @Test
    public void successAttachWithPOJO(){
        AttachRequest attachRequest = new AttachRequest(calcID, "Документ, удостоверяющий личность");

        AttachResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(attachRequest)
//                .log().all()
                .when()
                .post(ConstantValues.ATTACH_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(AttachResponse.class)
        ;

        assertEquals(sessionToken, response.getAccID());

    }



}
