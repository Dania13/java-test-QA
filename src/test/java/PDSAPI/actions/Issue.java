package PDSAPI.actions;

import PDSAPI.models.Error;
import PDSAPI.models.IssueRequest;
import PDSAPI.models.IssueResponse;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue {
    public static void IssuePolicy(String BaseUrl, String sessionToken, String policyID) {
        IssueRequest request = IssueRequest.builder()
                .policyID(policyID).build();

        IssueResponse response = given()
                .baseUri(BaseUrl)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
//                .log().all()
                .when()
                .post(ConstantValues.ISSUE_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(IssueResponse.class)
                ;

        assertEquals(sessionToken, response.getAccID());
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
