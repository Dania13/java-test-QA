package PDSAPI.actions;

import PDSAPI.models.AnnulRequest;
import PDSAPI.models.AnnulResponse;
import PDSAPI.models.Error;
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
 * Класс для выполнения операции аннулирования (отмены) страхового полиса.
 * <p>Содержит методы для отправки запроса на аннулирование полиса
 * и проверки корректности ответа от API.</p>
 *
 * <p>Аннулирование полиса позволяет отменить ранее
 * оформленный полис с указанием причины отмены.</p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * String sessionToken = "abc123";
 * String calcID = "calc456";
 * Annul.AnnulPolicy(sessionToken, calcID);
 * </pre>
 */
public class Annul {

    private static final Logger log = LoggerFactory.getLogger(Annul.class);

    /** Причина аннулирования по умолчанию для тестов */
    private static final String DEFAULT_REASON = "Тестовое аннулирование";

    /**
     * Приватный конструктор для утилитарного класса.
     */
    private Annul() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Выполняет аннулирование полиса по указанному идентификатору расчёта.
     * <p>Метод отправляет POST-запрос на эндпоинт аннулирования, проверяет
     * статус ответа и валидирует полученные данные.</p>
     *
     * <p><b>Шаги проверки:</b>
     * <ol>
     *   <li>Проверка HTTP статуса 200 OK</li>
     *   <li>Проверка наличия accID в ответе</li>
     *   <li>Проверка соответствия calcID в запросе и ответе</li>
     *   <li>Проверка успешности операции (response.isOk())</li>
     *   <li>Проверка отсутствия ошибок в ответе</li>
     * </ol>
     * </p>
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param calcID       идентификатор расчёта, который необходимо аннулировать
     * @throws AssertionError если:
     *         <ul>
     *           <li>статус код ответа не равен 200</li>
     *           <li>accID отсутствует или пуст</li>
     *           <li>calcID не совпадает с calcID из ответа</li>
     *           <li>операция аннулирования не успешна (isOk = false)</li>
     *           <li>присутствуют ошибки в ответе API</li>
     *         </ul>
     */
    @Step("Аннулирование полиса с calcID = {calcID}")
    public static void AnnulPolicy(String sessionToken, String calcID) {
        AnnulPolicy(sessionToken, calcID, DEFAULT_REASON);
    }

    /**
     * Выполняет аннулирование полиса с указанием причины.
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param calcID       идентификатор расчёта, который необходимо аннулировать
     * @param reason       причина аннулирования
     * @throws AssertionError если операция аннулирования не успешна
     */
    @Step("Аннулирование полиса с calcID = {calcID} по причине: {reason}")
    public static void AnnulPolicy(String sessionToken, String calcID, String reason) {
        log.info("Аннулирование полиса. calcID: {}, AccID: {}", calcID, maskToken(sessionToken));

        // Формирование запроса на аннулирование
        AnnulRequest request = AnnulRequest.builder()
                .calcID(calcID)
                .reason(reason)
                .build();

        // Выполнение запроса и получение ответа
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
                .as(AnnulResponse.class);

        // Валидация ответа
        validateResponse(response, calcID);

        log.info("Полис успешно аннулирован. calcID: {}, AccID: {}", calcID, maskToken(response.getAccID()));
    }

    /**
     * Валидирует ответ API после аннулирования.
     *
     * @param response ответ API
     * @param calcID   ожидаемый идентификатор расчёта
     * @throws AssertionError если валидация не пройдена
     */
    private static void validateResponse(AnnulResponse response, String calcID) {
        // Проверка наличия accID
        assertNotNull(response.getAccID(), "accID в ответе не должен быть null");
        assertFalse(response.getAccID().isEmpty(), "accID в ответе не должен быть пустым");

        // Проверка соответствия calcID
        assertEquals(calcID, response.getCalcID(),
                String.format("calcID запроса (%s) должен совпадать с calcID ответа (%s)",
                        calcID, response.getCalcID()));

        // Проверка успешности операции и отсутствия ошибок
        if (!response.isOk() || response.getErrors() != null) {
            String errorMessage = buildErrorMessage(response);
            throw new AssertionError(errorMessage);
        }
    }

    /**
     * Формирует сообщение об ошибке из ответа API.
     *
     * @param response ответ API с ошибками
     * @return отформатированное сообщение об ошибке
     */
    private static String buildErrorMessage(AnnulResponse response) {
        StringBuilder errorMessages = new StringBuilder("Ошибка при аннулировании полиса: ");

        if (response.getErrors() != null && response.getErrors().getErrors() != null) {
            for (Error error : response.getErrors().getErrors()) {
                errorMessages.append("\n- ")
                        .append(error.getMessage());
                if (error.getDetailMessage() != null) {
                    errorMessages.append(": ").append(error.getDetailMessage());
                }
            }
        } else {
            errorMessages.append("Неизвестная ошибка");
        }

        errorMessages.append(" [calcID: ").append(response.getCalcID()).append("]");
        return errorMessages.toString();
    }
}