package PDSAPI.actions;

import PDSAPI.models.*;
import PDSAPI.models.Error;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для выполнения операции импорта страхового полиса в систему.
 * <p>Содержит методы для импорта полисов из внешних систем, а также
 * вспомогательные методы для извлечения данных из ответа импорта.</p>
 *
 * <p>Процесс импорта полиса:
 * <ol>
 *   <li>Формирование объекта {@link PolicyImport} с данными полиса</li>
 *   <li>Отправка POST-запроса на эндпоинт импорта</li>
 *   <li>Получение ответа с результатами импорта</li>
 *   <li>Извлечение идентификаторов полиса и расчёта</li>
 * </ol>
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * // Создание полиса для импорта
 * PolicyImport policy = new CreatePolicy().getPolicy();
 *
 * // Импорт полиса
 * ImportResponse response = Import.importPolicy(sessionToken, policy);
 *
 * // Получение идентификаторов
 * String calcId = Import.getCalcId(response);
 * String policyId = Import.getPolicyId(response);
 * String policyNumber = Import.getNumber(response);
 * </pre>
 *
 * @author QA Team
 * @version 1.0
 * @since 2024
 */
public class Import {

    private static final Logger log = LoggerFactory.getLogger(Import.class);

    /**
     * Приватный конструктор для утилитарного класса.
     */
    private Import() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Выполняет импорт (сохранение) страхового полиса в системе.
     * <p>Метод отправляет POST-запрос на эндпоинт импорта, сохраняет полис
     * с выполненным расчётом и возвращает результат импорта.</p>
     *
     * <p><b>Шаги проверки:</b>
     * <ol>
     *   <li>Проверка HTTP статуса 200 OK</li>
     *   <li>Проверка отсутствия предупреждений (warnings)</li>
     *   <li>При наличии предупреждений - формирование детального сообщения об ошибке</li>
     * </ol>
     * </p>
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param policy       объект полиса для импорта, созданный с помощью {@link PolicyImport Builder}
     * @return объект {@link ImportResponse} с результатами импорта
     * @throws AssertionError если:
     *         <ul>
     *           <li>статус код ответа не равен 200</li>
     *           <li>присутствуют предупреждения (warnings)</li>
     *           <li>ответ содержит ошибки</li>
     *         </ul>
     */
    @Step("Сохранение полиса с расчётом")
    public static ImportResponse importPolicy(String sessionToken, PolicyImport policy) {
        log.info("Импорт полиса. Сессия: {}, Полис: {}", maskToken(sessionToken), policy.getProduct().getName());

        // Формирование запроса на импорт
        ImportRequest request = ImportRequest.builder()
                .policy(policy)
                .build();

        // Выполнение запроса и получение ответа
        ImportResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .body(request)
        .when()
                .post(ConstantValues.IMPORT_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(ImportResponse.class);

        log.debug("Импорт выполнен. CalcID: {}, PolicyID: {}",
                response.getPolicy() != null ? response.getPolicy().getCalcID() : "null",
                response.getPolicy() != null ? response.getPolicy().getID() : "null");

        // Валидация ответа
        validateImportResponse(response);

        log.info("Импорт полиса успешно завершен");
        return response;
    }

    /**
     * Выполняет импорт полиса с дополнительной проверкой на наличие ошибок.
     * <p>В отличие от метода {@link #importPolicy(String, PolicyImport)}, этот метод
     * также проверяет наличие ошибок (errors) в ответе, не только предупреждений.</p>
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param policy       объект полиса для импорта
     * @return объект {@link ImportResponse} с результатами импорта
     * @throws AssertionError если присутствуют ошибки или предупреждения
     */
    @Step("Сохранение полиса с расчётом (строгая проверка)")
    public static ImportResponse importPolicyStrict(String sessionToken, PolicyImport policy) {
        ImportResponse response = importPolicy(sessionToken, policy);

        // Дополнительная проверка на наличие ошибок
        if (response.getErrors() != null && response.getErrors().getErrors() != null
                && !response.getErrors().getErrors().isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Ошибки при импорте полиса:\n");
            for (Error error : response.getErrors().getErrors()) {
                errorMsg.append("- ").append(error.getMessage())
                        .append(": ").append(error.getDetailMessage()).append("\n");
            }
            throw new AssertionError(errorMsg.toString());
        }

        return response;
    }

    /**
     * Извлекает идентификатор расчёта (calcID) из ответа импорта.
     * <p>CalcID используется для последующих операций с расчётом,
     * таких как аннулирование, печать документов и т.д.</p>
     *
     * @param response объект {@link ImportResponse} от успешного импорта
     * @return строка с идентификатором расчёта
     * @throws NullPointerException если response или policy в ответе равны null
     */
    @Step("Получение CalcID ({response.policy.calcID})")
    public static String getCalcId(ImportResponse response) {
        assertNotNull(response, "ImportResponse не должен быть null");
        assertNotNull(response.getPolicy(), "Policy в ответе не должен быть null");

        String calcId = response.getPolicy().getCalcID();
        assertNotNull(calcId, "CalcID не должен быть null");
        assertFalse(calcId.isEmpty(), "CalcID не должен быть пустым");

        log.debug("Извлечен CalcID: {}", calcId);
        Allure.addAttachment("CalcID", "text/plain", calcId, "txt");
        return calcId;
    }

    /**
     * Извлекает идентификатор полиса (policyID) из ответа импорта.
     * <p>PolicyID используется для идентификации полиса в системе
     * при выполнении операций с полисом.</p>
     *
     * @param response объект {@link ImportResponse} от успешного импорта
     * @return строка с идентификатором полиса
     * @throws NullPointerException если response или policy в ответе равны null
     */
    @Step("Получение PolicyID ({response.policy.ID})")
    public static String getPolicyId(ImportResponse response) {
        assertNotNull(response, "ImportResponse не должен быть null");
        assertNotNull(response.getPolicy(), "Policy в ответе не должен быть null");

        String policyId = response.getPolicy().getID();
        assertNotNull(policyId, "PolicyID не должен быть null");
        assertFalse(policyId.isEmpty(), "PolicyID не должен быть пустым");

        log.debug("Извлечен PolicyID: {}", policyId);
        Allure.addAttachment("PolicyID", "text/plain", policyId, "txt");
        return policyId;
    }

    /**
     * Извлекает номер полиса из ответа импорта.
     * <p>Номер полиса - это внешний идентификатор, который отображается
     * в документах и используется для поиска полиса.</p>
     *
     * @param response объект {@link ImportResponse} от успешного импорта
     * @return строка с номером полиса
     * @throws NullPointerException если response или policy в ответе равны null
     */
    @Step("Получение номера полиса ({response.policy.number})")
    public static String getNumber(ImportResponse response) {
        assertNotNull(response, "ImportResponse не должен быть null");
        assertNotNull(response.getPolicy(), "Policy в ответе не должен быть null");

        String number = response.getPolicy().getNumber();
        assertNotNull(number, "Номер полиса не должен быть null");
        assertFalse(number.isEmpty(), "Номер полиса не должен быть пустым");

        log.debug("Извлечен номер полиса: {}", number);
        Allure.addAttachment("Номер полиса", "text/plain", number, "txt");
        return number;
    }

    /**
     * Извлекает все идентификаторы из ответа импорта одновременно.
     * <p>Удобный метод для получения всех идентификаторов за один вызов.</p>
     *
     * @param response объект {@link ImportResponse} от успешного импорта
     * @return объект {@link ImportIdentifiers} со всеми идентификаторами
     */
    @Step("Получение всех идентификаторов из ответа импорта")
    public static ImportIdentifiers getAllIdentifiers(ImportResponse response) {
        return ImportIdentifiers.builder()
                .calcId(getCalcId(response))
                .policyId(getPolicyId(response))
                .policyNumber(getNumber(response))
                .build();
    }

    /**
     * Проверяет, был ли импорт успешным.
     *
     * @param response объект {@link ImportResponse}
     * @return true, если импорт успешен (нет ошибок и предупреждений)
     */
    public static boolean isImportSuccessful(ImportResponse response) {
        if (response == null || response.getPolicy() == null) {
            return false;
        }

        boolean hasNoErrors = response.getErrors() == null
                || response.getErrors().getErrors() == null
                || response.getErrors().getErrors().isEmpty();

        boolean hasNoWarnings = response.getWarnings() == null
                || response.getWarnings().getErrors() == null
                || response.getWarnings().getErrors().isEmpty();

        return hasNoErrors && hasNoWarnings;
    }

    /**
     * Валидирует ответ импорта на наличие предупреждений.
     *
     * @param response объект {@link ImportResponse} для валидации
     * @throws AssertionError если присутствуют предупреждения
     */
    private static void validateImportResponse(ImportResponse response) {
        assertNotNull(response, "ImportResponse не должен быть null");
        assertNotNull(response.getPolicy(), "Policy в ответе не должен быть null");

        // Проверка на наличие предупреждений
        if (response.getWarnings() != null && response.getWarnings().getErrors() != null
                && !response.getWarnings().getErrors().isEmpty()) {

            StringBuilder warningMessages = new StringBuilder();
            warningMessages.append("Предупреждения при импорте полиса:\n");

            for (Error warning : response.getWarnings().getErrors()) {
                String message = warning.getDetailMessage() != null
                        ? warning.getDetailMessage()
                        : warning.getMessage();
                warningMessages.append("- ").append(message).append("\n");
            }

            warningMessages.append("CalcID: ").append(response.getPolicy().getCalcID());

            log.warn("Импорт выполнен с предупреждениями: {}", warningMessages.toString());
            throw new AssertionError(warningMessages.toString().trim());
        }
    }

    /**
     * Маскирует токен для безопасного логирования.
     *
     * @param token оригинальный токен
     * @return замаскированный токен
     */
    private static String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }

    /**
     * Вспомогательный класс для хранения всех идентификаторов полиса.
     */
    @Getter
    public static class ImportIdentifiers {
        private final String calcId;
        private final String policyId;
        private final String policyNumber;

        private ImportIdentifiers(String calcId, String policyId, String policyNumber) {
            this.calcId = calcId;
            this.policyId = policyId;
            this.policyNumber = policyNumber;
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public String toString() {
            return String.format("ImportIdentifiers{calcId='%s', policyId='%s', policyNumber='%s'}",
                    calcId, policyId, policyNumber);
        }

        public static class Builder {
            private String calcId;
            private String policyId;
            private String policyNumber;

            public Builder calcId(String calcId) {
                this.calcId = calcId;
                return this;
            }

            public Builder policyId(String policyId) {
                this.policyId = policyId;
                return this;
            }

            public Builder policyNumber(String policyNumber) {
                this.policyNumber = policyNumber;
                return this;
            }

            public ImportIdentifiers build() {
                return new ImportIdentifiers(calcId, policyId, policyNumber);
            }
        }
    }
}