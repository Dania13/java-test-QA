package PDSAPI.actions;

import PDSAPI.models.AttachRequest;
import PDSAPI.models.AttachResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttachDocs {
    public static void AttachDocs(String BaseUrl, String Endpoint, String sessionToken, String calcID, String typeDoc) {
        AttachRequest attachRequest = new AttachRequest(calcID, typeDoc);

        AttachResponse response = given()
                .baseUri(BaseUrl)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(attachRequest)
//                .log().all()
                .when()
                .post(Endpoint).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(AttachResponse.class)
                ;

        assertEquals(sessionToken, response.getAccID());

    }
}
