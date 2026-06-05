package PDSAPI.tests;

import PDSAPI.actions.Auth;
import helpers.CreatePolicy;
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
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        ImportResponse response = Import.getImportResponse(sessionToken, policy);
        calcID = Import.getCalcID(response);
    }


    @Test
    public void successAttachWithPOJO(){
        AttachRequest request = AttachRequest.builder()
                .calcID(calcID)
                .fileName("test.txt")
                .type("Документ, удостоверяющий личность")
                .comment("тестовый документ")
                .attachment("0KLQtdGB0YLQvtCy0YvQuSDQtNC+0LrRg9C80LXQvdGC").build();

        AttachResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
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
