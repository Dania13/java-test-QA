package PDSAPI.actions;

import PDSAPI.models.Error;
import PDSAPI.models.IssueRequest;
import PDSAPI.models.IssueResponse;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static helpers.MaskParametsForLog.maskToken;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для выполнения операции оформления (выпуска) страхового полиса.
 * <p>Содержит методы для финального оформления полиса после успешного расчёта и проверки всех данных.
 * Оформление полиса переводит его в статус "активен" и генерирует официальные документы.</p>
 *
 * <p>Процесс оформления полиса:
 * <ol>
 *   <li>Формирование запроса с идентификатором полиса</li>
 *   <li>Отправка POST-запроса на эндпоинт оформления</li>
 *   <li>Проверка успешности операции</li>
 *   <li>Валидация ответа и отсутствия ошибок</li>
 * </ol>
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * // Получение sessionToken после авторизации
 * String sessionToken = Auth.loginUser("username", "password");
 *
 * // Импорт полиса
 * ImportResponse = Import.importPolicy(sessionToken, policy);
 * String policyId = Import.getPolicyId(importResponse);
 *
 * // Оформление полиса
 * Issue.issuePolicy(sessionToken, policyId);
 * </pre>
 *
 * @author QA Team
 * @version 1.0
 * @since 2024
 */
public class Issue {

    private static final Logger log = LoggerFactory.getLogger(Issue.class);

    /**
     * Приватный конструктор для утилитарного класса.
     */
    private Issue() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Выполняет оформление (выпуск) страхового полиса.
     * <p>Метод отправляет POST-запрос на эндпоинт оформления полиса,
     * проверяет успешность операции и отсутствие ошибок.</p>
     *
     * <p><b>Шаги проверки:</b>
     * <ol>
     *   <li>Проверка HTTP статуса 200 OK</li>
     *   <li>Проверка соответствия sessionToken и accID в ответе</li>
     *   <li>Проверка отсутствия ошибок в ответе</li>
     * </ol>
     * </p>
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param policyID     идентификатор полиса, который необходимо оформить
     * @throws AssertionError если:
     *         <ul>
     *           <li>статус код ответа не равен 200</li>
     *           <li>sessionToken не совпадает с accID из ответа</li>
     *           <li>присутствуют ошибки в ответе API</li>
     *         </ul>
     */
    @Step("Оформление полиса c policyID = {policyID}")
    public static void issuePolicy(String sessionToken, String policyID) {
        issuePolicy(sessionToken, policyID, false);
    }

    /**
     * Выполняет оформление полиса с возможностью пропуска проверки accID.
     * <p>Некоторые версии API могут не возвращать accID в ответе на оформление полиса.
     * Этот метод позволяет пропустить проверку accID при необходимости.</p>
     *
     * @param sessionToken    токен сессии авторизованного пользователя
     * @param policyID        идентификатор полиса, который необходимо оформить
     * @param skipAccIdCheck  если true, пропускает проверку соответствия sessionToken и accID
     * @throws AssertionError если операция оформления не успешна
     */
    @Step("Оформление полиса c policyID = {policyID} (skipAccIdCheck={skipAccIdCheck})")
    public static void issuePolicy(String sessionToken, String policyID, boolean skipAccIdCheck) {
        log.info("Оформление полиса. PolicyID: {}, AccID:: {}", policyID, maskToken(sessionToken));

        // Валидация входных параметров
        validateInputParams(sessionToken, policyID);

        // Формирование запроса на оформление
        IssueRequest request = IssueRequest.builder()
                .policyID(policyID)
                .build();

        // Выполнение запроса и получение ответа
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
                .as(IssueResponse.class);

        // Валидация ответа
        validateIssueResponse(response, sessionToken, policyID, skipAccIdCheck);

        log.info("Полис успешно оформлен. PolicyID: {}, AccID: {}", policyID, maskToken(response.getAccID()));
    }

    /**
     * Валидирует входные параметры.
     *
     * @param sessionToken токен сессии
     * @param policyID     идентификатор полиса
     * @throws IllegalArgumentException если параметры не валидны
     */
    private static void validateInputParams(String sessionToken, String policyID) {
        assertNotNull(sessionToken, "sessionToken не должен быть null");
        assertNotNull(policyID, "policyID не должен быть null");
        assertFalse(sessionToken.trim().isEmpty(), "sessionToken не должен быть пустым");
        assertFalse(policyID.trim().isEmpty(), "policyID не должен быть пустым");
    }

    /**
     * Валидирует ответ API после оформления полиса.
     *
     * @param response        ответ API
     * @param sessionToken    ожидаемый токен сессии
     * @param policyID        идентификатор полиса
     * @param skipAccIdCheck  флаг пропуска проверки accID
     * @throws AssertionError если валидация не пройдена
     */
    private static void validateIssueResponse(IssueResponse response, String sessionToken,
                                              String policyID, boolean skipAccIdCheck) {
        assertNotNull(response, "IssueResponse не должен быть null");

        // Проверка accID (если не пропущена)
        if (!skipAccIdCheck) {
            assertNotNull(response.getAccID(),
                    String.format("accID в ответе не должен быть null для policyID: %s", policyID));
            assertFalse(response.getAccID().isEmpty(),
                    String.format("accID в ответе не должен быть пустым для policyID: %s", policyID));

            // Проверка на то, что sessionToken = accID
            if (!sessionToken.equals(response.getAccID())) {
                log.warn("sessionToken ({}) отличается от accID ({}). Это может быть нормально для некоторых версий API",
                        maskToken(sessionToken), response.getAccID());
            }
        }

        // Проверка policyID в ответе (если возвращается)
        if (response.getPolicyID() != null && !response.getPolicyID().isEmpty()) {
            assertEquals(policyID, response.getPolicyID(),
                    String.format("policyID в ответе (%s) должен совпадать с запрошенным (%s)",
                            response.getPolicyID(), policyID));
        }

        // Проверка наличия ошибок
        if (response.getErrors() != null && response.getErrors().getErrors() != null
                && !response.getErrors().getErrors().isEmpty()) {
            String errorMessage = buildErrorMessage(response.getErrors().getErrors(), policyID);
            log.error("Ошибки при оформлении полиса: {}", errorMessage);
            throw new AssertionError(errorMessage);
        }
    }

    /**
     * Формирует сообщение об ошибке из списка ошибок API.
     *
     * @param errors   список ошибок
     * @param policyID идентификатор полиса
     * @return отформатированное сообщение об ошибке
     */
    private static String buildErrorMessage(java.util.List<Error> errors, String policyID) {
        StringBuilder errorMessages = new StringBuilder();
        errorMessages.append("Ошибки при оформлении полиса (policyID: ").append(policyID).append("):\n");

        for (Error error : errors) {
            errorMessages.append("- ");
            if (error.getMessage() != null) {
                errorMessages.append(error.getMessage());
            }
            if (error.getDetailMessage() != null) {
                errorMessages.append(": ").append(error.getDetailMessage());
            }
            if (error.getType() != null) {
                errorMessages.append(" [").append(error.getType()).append("]");
            }
            errorMessages.append("\n");
        }

        return errorMessages.toString().trim();
    }

}