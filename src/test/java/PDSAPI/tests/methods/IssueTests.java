package PDSAPI.tests.methods;

import PDSAPI.actions.Attach;
import PDSAPI.actions.Auth;
import PDSAPI.actions.Issue;
import helpers.CreatePolicy;
import PDSAPI.actions.Import;
import PDSAPI.models.*;
import PDSAPI.models.Error;
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
 * Тесты для проверки метода оформления
 */
@Epic("Проверка API методов продукта")
@Feature("Метод оформления")
public class IssueTests {
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
     * Позитивный тест оформления
     */
    @Test
    @Description("Оформление полиса в статусе Проект")
    public void successIssueWithPOJO(){
        String[] REQUIRED_DOCUMENTS = {
                "Документ, удостоверяющий личность",
                "Анкета для проведения идентификации клиента",
                "Согласие на обработку ПД",
                "Согласие на доп. услугу"
        };

        // Прикрепление документов
        Attach.AttachDocs(sessionToken, calcID, REQUIRED_DOCUMENTS);

        // Создание объекта для отправки полиса на оформление
        IssueRequest request = IssueRequest.builder()
                .policyID(policyID).build();

        // Оформление полиса
        IssueResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.ISSUE_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(IssueResponse.class)
        ;

        // Проверка, что полученный в ответе AccID равен передаваемому
        assertEquals(sessionToken, response.getAccID(), "Полученный AccID не равен sessionToken от авториазации");

        // Проверка того, что нет получаемых ошибок
        try {
            assertNull(response.getErrors());
        } catch (AssertionError e) {
            StringBuilder errorMessages = new StringBuilder();
            assert response.getErrors() != null;
            for (Error error : response.getErrors().getErrors()) {
                errorMessages.append(error.getMessage()).append("\n");
            }

            throw new AssertionError(errorMessages.toString().trim()+ " " + calcID);
        }
    }

    /**
     * Негативный тест оформления повторно
     */
    @Test
    @Description("Оформление полиса в статусе Оформлен")
    public void badIssueWithPOJO(){
        String[] REQUIRED_DOCUMENTS = {
                "Документ, удостоверяющий личность",
                "Анкета для проведения идентификации клиента",
                "Согласие на обработку ПД",
                "Согласие на доп. услугу"
        };

        // Прикрепление документов
        Attach.AttachDocs(sessionToken, calcID, REQUIRED_DOCUMENTS);

        // Создание объекта для отправки полиса на оформление
        IssueRequest request = IssueRequest.builder()
                .policyID(policyID).build();

        // Первичное оформление полиса
        Issue.issuePolicy(sessionToken, policyID);

        // Повторное оформление полиса
        IssueResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
                .when()
                .post(ConstantValues.ISSUE_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(IssueResponse.class)
                ;

        // Проверка, что полученный в ответе AccID равен передаваемому
        assertEquals(sessionToken, response.getAccID(), "Полученный AccID не равен sessionToken от авториазации");

        // Проверка, что есть блок с ошибками
        assertNotNull(response.getErrors(), "Нет блока ошибок");
    }
}
