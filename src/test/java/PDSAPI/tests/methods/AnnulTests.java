package PDSAPI.tests.methods;

import PDSAPI.actions.*;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import PDSAPI.tests.BaseTest;
import helpers.CreatePolicy;
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
 * Тесты для проверки метода аннулирования
 */
@Epic("Проверка API методов продукта")
@Feature("Метод Аннулирование")
public class AnnulTests extends BaseTest {
    private String sessionToken, calcID, policyID;

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
        // Получение policyID из сохранённого полиса
        policyID = Import.getPolicyId(response);
    }

    /**
     * Позитивный тест аннулирования
     */
    @Test
    @Description("Аннулирование полиса в статусе Оформлен")
    public void successAnnulWithPOJO(){
        // Типы документов, необходимые для оформления полиса/
        String[] REQUIRED_DOCUMENTS = {
                "Документ, удостоверяющий личность",
                "Анкета для проведения идентификации клиента",
                "Согласие на обработку ПД",
                "Согласие на доп. услугу"
        };
        // Прикрепление документов
        Attach.AttachDocs(sessionToken, calcID, REQUIRED_DOCUMENTS);
        // Оформление полиса
        Issue.issuePolicy(sessionToken, policyID);
        // Создание объекта для отправки полиса на аннулирование
        AnnulRequest request = AnnulRequest.builder()
                .calcID(calcID)
                .reason("Тест").build();

        // Аннулирование полиса
        AnnulResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.ANNUL_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(AnnulResponse.class)
                ;

        // Проверка, что полученный в ответе AccID равен передаваемому
        assertEquals(sessionToken, response.getAccID(), "Полученный AccID не равен sessionToken от авториазации");
        // Проверка, что полученный в ответе CalcID равен передаваемому
        assertEquals(calcID, response.getCalcID(), "Полученный calcID не равен calcID от сохранения");

        // Проверка того, что нет получаемых ошибок
        try {
            assertTrue(response.isOk());
            assertNull(response.getErrors());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getErrors() != null;
            for (Error error : response.getErrors().getErrors()) {
                errorMessages.append(error.getMessage()).append("\n");
            }
            throw new AssertionError(errorMessages.toString().trim() + " " + calcID);
        }

    }

    /**
     * Негативный тест аннулирования проекта
     */
    @Test
    @Description("Аннулирование полиса в статусе Проект")
    public void badAnnulDraftPOJO(){
        // Создание объекта для отправки полиса на аннулирование
        AnnulRequest request = AnnulRequest.builder()
                .calcID(calcID)
                .reason("Тест").build();
        // Аннулирование полиса
        AnnulResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.ANNUL_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(AnnulResponse.class)
                ;


        // Проверка, что полученный в ответе AccID равен передаваемому
        assertEquals(sessionToken, response.getAccID(), "Полученный AccID не равен sessionToken от авториазации");
        // Проверка, что полученный в ответе CalcID равен передаваемому
        assertEquals(calcID, response.getCalcID(), "Полученный calcID не равен calcID от сохранения");
        // Проверка, что в поле Ok пришёл False
        assertFalse(response.isOk(), "В поле Ok пришёл True");
        // Проверка, что есть блок с ошибками
        assertNotNull(response.getErrors(), "Нет блока ошибок");
    }

}
