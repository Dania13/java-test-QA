package PDSAPI.actions;

import PDSAPI.models.AnnulRequest;
import PDSAPI.models.AnnulResponse;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class Annul {
    public static void AnnulPolicy(String sessionToken, String calcID) {
        AnnulRequest request = AnnulRequest.builder()
                .calcID(calcID)
                .reason("Тест")
                .build();

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
}
