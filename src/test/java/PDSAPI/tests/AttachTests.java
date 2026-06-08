package PDSAPI.tests;

import PDSAPI.actions.Auth;
import helpers.CreatePolicy;
import PDSAPI.actions.Import;
import PDSAPI.models.*;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@Epic("Проверка API методов продукта")
@Feature("Метод Прикрепление документов")
public class AttachTests {
    private String sessionToken, calcID;

    @BeforeEach
    @Step("Предустановка")
    public void setUp() {
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        ImportResponse response = Import.importPolicy(sessionToken, new CreatePolicy().getPolicy());
        calcID = Import.getCalcId(response);
    }


    @Test
    @Description("Успешное прикрепление документа в полис в статусе Проект")
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
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.ATTACH_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(AttachResponse.class)
        ;

        assertEquals(sessionToken, response.getAccID());

    }

}
