package PDSAPI.tests.methods;

import PDSAPI.actions.*;
import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
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
 * Тесты для проверки метода печати
 */
@Epic("Проверка API методов продукта")
@Feature("Метод Печати полиса")
public class PrintTests {
    private String sessionToken, calcID, policyID, number;

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
        // Получение номера из сохранённого полиса
        number = Import.getNumber(response);
        // Типы документов, необходимые для оформления полиса/
        String[] REQUIRED_DOCUMENTS = {
                "Документ, удостоверяющий личность",
                "Анкета для проведения идентификации клиента",
                "Согласие на обработку ПД",
                "Согласие на доп. услугу"
        };
        // Прикрепление документов
        Attach.AttachDocs(sessionToken, calcID, REQUIRED_DOCUMENTS);
    }

    /**
     * Позитивный тест печати оформленного полиса
     */
    @Test
    @Description("Печать оформленного полиса")
    public void successPrintWithPOJO(){
        // Оформление полиса
        Issue.issuePolicy(sessionToken, policyID);

        // Создание объекта для отправки полиса на печать
        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Печать").build();

        // Печать полиса
        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.PRINT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(PrintResponse.class)
        ;

        //  Проверка, что полученный в ответе number равен передаваемому
        assertEquals(number, response.getNumber(), "Полученный номер не равен тому, что пришёл от сохранения");

        // Проверка, что в ответе пришёл URL
        assertNotNull(response.getUrl(), "В ответе нет ссылки на скачивание документов");

        // Проверка того, что нет получаемых ошибок
        try {
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
     * Позитивный тест печати Черновика для проекта
     */
    @Test
    @Description("Печать Проекта")
    public void successPrintDraftPOJO(){
        // Создание объекта для отправки полиса на печать
        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Черновик").build();

        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.PRINT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(PrintResponse.class)
                ;

        //  Проверка, что полученный в ответе number равен передаваемому
        assertEquals(number, response.getNumber(), "Полученный номер не равен тому, что пришёл от сохранения");

        // Проверка, что в ответе пришёл URL
        assertNotNull(response.getUrl(), "В ответе нет ссылки на скачивание документов");

        // Проверка того, что нет получаемых ошибок
        try {
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
     * Негативный тест печати Черновика для оформленного полиса
     */
    @Test
    @Description("Печать черновика для оформленного полиса")
    public void badPrintDraftPOJO(){
        // Оформление полиса
        Issue.issuePolicy(sessionToken, policyID);

        // Создание объекта для отправки полиса на печать
        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Черновик").build();

        // Печать полиса
        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
                .when()
                .post(ConstantValues.PRINT_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(PrintResponse.class)
                ;

        //  Проверка, что полученный в ответе number равен передаваемому
        assertNull(response.getNumber(), "В ответе пришёл номер полиса");

        // Проверка, что в ответе пришёл URL
        assertNull(response.getUrl(), "В ответе пришла ссылка на скачивание документов");

        // Проверка, что есть блок с ошибками
        assertNotNull(response.getErrors(), "Нет блока ошибок");
    }

    /**
     * Позитивный тест оформления полиса через печать
     */
    @Test
    @Description("Оформление полиса через печать")
    public void badPrint(){
        // Создание объекта для отправки полиса на печать
        PrintRequest request = PrintRequest.builder()
                .calcID(calcID)
                .type("Печать").build();

        // Печать полиса
        PrintResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
                .when()
                .post(ConstantValues.PRINT_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .as(PrintResponse.class)
                ;

        //  Проверка, что полученный в ответе number равен передаваемому
        assertEquals(number, response.getNumber(), "Полученный номер не равен тому, что пришёл от сохранения");

        // Проверка, что в ответе пришёл URL
        assertNotNull(response.getUrl(), "В ответе нет ссылки на скачивание документов");

        // Проверка того, что нет получаемых ошибок
        try {
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
}
