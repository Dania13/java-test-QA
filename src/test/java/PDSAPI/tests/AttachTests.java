package PDSAPI.tests;

import PDSAPI.actions.Auth;
import PDSAPI.actions.CreatePolicy;
import PDSAPI.actions.Import;
import PDSAPI.models.*;
import PDSAPI.models.Object;
import PDSAPI.specs.ConstantValues;
import helpers.InnGenerator;
import helpers.SNILSGenerator;
import io.restassured.http.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class AttachTests {
    private String sessionToken, calcID;
    PolicyImport policy = new CreatePolicy().getPolicy();

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.BASE_URL,
                ConstantValues.AUTH_ENDPOINT,
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        calcID = Import.ImportPolicy(ConstantValues.BASE_URL, ConstantValues.IMPORT_ENDPOINT, sessionToken, policy);
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
