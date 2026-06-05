package PDSAPI.actions;

import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Import {

    public static ImportResponse ImportPolicy (String sessionToken, PolicyImport policy) {
        ImportRequest request = ImportRequest.builder()
                .policy(policy).build();
        ImportResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
//                .log().all()
                .body(request).
                when()
                .post(ConstantValues.IMPORT_ENDPOINT).
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
        return response;
    }

    public static ImportResponse getImportResponse(String sessionToken, PolicyImport policy) {
        return ImportPolicy(sessionToken, policy);
    }

    public static String getCalcID(ImportResponse response) {
            return response.getPolicy().getCalcID();
    }

    public static String getPolicyID(ImportResponse response) {
        return response.getPolicy().getID();
    }

    public static String getNumber(ImportResponse response) {
        return response.getPolicy().getNumber();
    }
}
