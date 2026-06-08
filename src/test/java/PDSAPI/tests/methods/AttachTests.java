package PDSAPI.tests.methods;

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

/**
 * Тесты для проверки метода прикрепления документов
 */
@Epic("Проверка API методов продукта")
@Feature("Метод Прикрепление документов")
public class AttachTests {
    private String sessionToken, calcID;

    /**
     * Предустановка с авторизацией и сохранением полиса
     */
    @BeforeEach
    @Step("Предустановка")
    public void setUp() {
        // Авторизация с закешированным токеном
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        // Сохранение
        ImportResponse response = Import.importPolicy(sessionToken, new CreatePolicy().getPolicy());
        // Получение calcID из сохранённого полиса
        calcID = Import.getCalcId(response);
    }

    /**
     * Позитивный тест прикрепления документа
     */
    @Test
    @Description("Успешное прикрепление документа в полис в статусе Проект")
    public void successAttachWithPOJO(){
        String typeDoc = "Документ, удостоверяющий личность";
        // Создание объекта для отправки документа для прикрепления
        AttachRequest request = AttachRequest.builder()
                .calcID(calcID)
                .fileName("test.txt")
                .type(typeDoc)
                .comment("тестовый документ")
                .attachment("0KLQtdGB0YLQvtCy0YvQuSDQtNC+0LrRg9C80LXQvdGC").build();

        // Прикрепление документа
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

        // Проверка, что полученный в ответе AccID равен передаваемому
        assertEquals(sessionToken, response.getAccID());

        // Проверка наличия docID
        assertTrue(response.getDocID() > 0,
                String.format("docID должен быть положительным числом. " +
                                "Получено: %d. Расчет: %s, Тип документа: %s",
                        response.getDocID(), calcID, typeDoc));

    }

    /**
     * Негативный тест прикрепления документа с несуществующим типом
     */
    @Test
    @Description("Прикрепление документа в полис с несуществующим типом")
    public void badAttachWithPOJO(){
        String typeDoc = "Test";

        // Создание объекта для отправки документа для прикрепления
        AttachRequest request = AttachRequest.builder()
                .calcID(calcID)
                .fileName("test.txt")
                .type(typeDoc)
                .comment("тестовый документ")
                .attachment("0KLQtdGB0YLQvtCy0YvQuSDQtNC+0LrRg9C80LXQvdGC").build();

        // Прикрепление документа
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
                .log().all()
                .extract()
                .as(AttachResponse.class)
                ;


        // Проверка, что полученный в ответе AccID равен передаваемому
        assertEquals(sessionToken, response.getAccID());

        // Проверка наличия docID
        assertNull(response.getDocID(),"docID не должен быть прийти");

        // Проверка, что есть блок с ошибками
        assertNotNull(response.getErrors(), "Нет блока ошибок");
    }

}
