package PDSAPI.actions;

import PDSAPI.models.AttachRequest;
import PDSAPI.models.AttachResponse;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Attach {
    @Step("Прикрепление документа {typeDoc}")
    public static void AttachDocs(String sessionToken, String calcID, String typeDoc) {

        AttachRequest request = AttachRequest.builder()
                .calcID(calcID)
                .fileName("test.txt")
                .type(typeDoc)
                .comment("тестовый документ")
                .attachment("0KLQtdGB0YLQvtCy0YvQuSDQtNC+0LrRg9C80LXQvdGC").build();

        AttachResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
                .when()
                .post(ConstantValues.ATTACH_ENDPOINT).
                then()
                .statusCode(200)
                .extract()
                .as(AttachResponse.class)
                ;

        assertEquals(sessionToken, response.getAccID());

    }
}
