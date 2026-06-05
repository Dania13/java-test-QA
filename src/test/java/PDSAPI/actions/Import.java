package PDSAPI.actions;

import PDSAPI.models.*;
import PDSAPI.models.Error;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Import {
    public static String ImportPolicy (String BaseUrl, String Endpoint, String sessionToken, PolicyImport policy) {
        ImportRequest user = ImportRequest.builder()
                .policy(policy).build();
        ImportResponse response = given()
                .baseUri(BaseUrl)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
//                .log().all()
                .body(user).
                when()
                .post(Endpoint).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(ImportResponse.class);

        try {
            assertNull(response.getWarnings());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getWarnings() != null;
            for (Error error : response.getWarnings().getErrors()) {
                errorMessages.append(error.getDetailMessage()).append("\n");
            }

            throw new AssertionError(errorMessages.toString().trim());
        }
        return response.getPolicy().getCalcID();
    }
}
