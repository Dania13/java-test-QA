package PDSAPI.actions;

import PDSAPI.models.AttachRequest;
import PDSAPI.models.AttachResponse;
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
 * Класс для выполнения операции прикрепления документов к расчёту или полису.
 * <p>Содержит методы для загрузки и привязки файлов (скан-копий документов)
 * к расчёту страхового полиса.</p>
 *
 * <p>Типы документов могут включать:
 * <ul>
 *   <li>Документ, удостоверяющий личность</li>
 *   <li>Анкета для проведения идентификации клиента</li>
 *   <li>Согласие на обработку ПД</li>
 *   <li>Согласие на доп. услугу</li>
 * </ul>
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * String sessionToken = "abc123";
 * String calcID = "calc456";
 * Attach.attachDocs(sessionToken, calcID, "Документ, удостоверяющий личность");
 * </pre>
 */
public class Attach {

    private static final Logger log = LoggerFactory.getLogger(Attach.class);

    /** Имя файла по умолчанию для тестов */
    private static final String DEFAULT_FILE_NAME = "test.txt";

    /** Комментарий по умолчанию для тестов */
    private static final String DEFAULT_COMMENT = "тестовый документ";

    /** Тестовое содержимое файла в Base64 (представляет собой тестовую строку) */
    private static final String TEST_ATTACHMENT = "0KLQtdGB0YLQvtCy0YvQuSDQtNC+0LrRg9C80LXQvdGC";

    /**
     * Прикрепляет документ к расчёту.
     * <p>Метод отправляет POST-запрос на эндпоинт прикрепления документов,
     * используя тестовые данные для файла.</p>
     *
     * <p><b>Шаги проверки:</b>
     * <ol>
     *   <li>Проверка HTTP статуса 200 OK</li>
     *   <li>Проверка соответствия sessionToken и accID в ответе</li>
     * </ol>
     * </p>
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param calcID       идентификатор расчёта, к которому прикрепляется документ
     * @param typeDoc      тип прикрепляемого документа (например, "PASSPORT", "SNILS")
     * @throws AssertionError если:
     *         <ul>
     *           <li>статус код ответа не равен 200</li>
     *           <li>sessionToken не совпадает с accID из ответа</li>
     *         </ul>
     */
    @Step("Прикрепление документа {typeDoc}")
    public static void AttachDocs(String sessionToken, String calcID, String typeDoc) {
        AttachDocs(sessionToken, calcID, typeDoc, DEFAULT_FILE_NAME, DEFAULT_COMMENT, TEST_ATTACHMENT);
    }

    /**
     * Прикрепляет документ к расчёту с пользовательскими параметрами файла.
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param calcID       идентификатор расчёта
     * @param typeDoc      тип документа
     * @param fileName     имя файла
     * @param comment      комментарий к документу
     * @param attachment   содержимое файла в формате Base64
     * @throws AssertionError если операция прикрепления не успешна
     */
    @Step("Прикрепление документа {typeDoc} с именем файла {fileName}")
    public static void AttachDocs(String sessionToken, String calcID, String typeDoc,
                                  String fileName, String comment, String attachment) {
        log.info("Прикрепление документа: {}, к полису с calcID: {} и accID: {}", typeDoc, calcID, maskToken(sessionToken));

        // Формирование запроса на прикрепление документа
        AttachRequest request = AttachRequest.builder()
                .calcID(calcID)
                .fileName(fileName)
                .type(typeDoc)
                .comment(comment)
                .attachment(attachment)
                .build();

        // Выполнение запроса и получение ответа
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
                .as(AttachResponse.class);

        // Валидация ответа
        validateResponse(response, sessionToken, calcID, typeDoc);
        log.info("Документ: {} прикреплён, к полису с calcID: {} и accID: {}", typeDoc, calcID, maskToken(sessionToken));
    }


    /**
     * Прикрепляет несколько документов к расчёту полиса.
     * <p>Метод последовательно прикрепляет все документы из переданного массива.
     * При возникновении ошибки прикрепления любого из документов выполнение метода
     * прерывается и выбрасывается исключение с указанием проблемного документа.</p>
     *
     * <p><b>Важно:</b> Документы прикрепляются в том порядке, в котором они указаны в массиве.
     * Если требуется прикрепить обязательные документы, рекомендуется передавать их
     * в порядке, соответствующем бизнес-логике.</p>
     *
     * <p>Пример использования:</p>
     * <pre>
     * String sessionToken = Auth.loginUser("user", "pass");
     * String calcID = "CALC123456";
     * String[] docs = {
     *     "Документ, удостоверяющий личность",
     *     "Анкета для проведения идентификации клиента",
     *     "Согласие на обработку ПД"
     * };
     * attachRequiredDocuments(sessionToken, calcID, docs);
     * </pre>
     *
     * @param sessionToken токен сессии авторизованного пользователя
     * @param calcID       идентификатор расчёта, к которому прикрепляются документы
     * @param documents    массив строк с типами документов для прикрепления
     * @throws AssertionError если не удалось прикрепить хотя бы один документ.
     *         Сообщение ошибки содержит тип документа, на котором произошёл сбой
     * @throws IllegalArgumentException если sessionToken, calcID или documents равны null
     * @see Attach#AttachDocs(String, String, String) для деталей прикрепления отдельного документа
     */
    @Step("Прикрепление документов к полису")
    public static void AttachDocs(String sessionToken, String calcID, String[] documents) {

        for (String docType : documents) {
            try {
                Attach.AttachDocs(sessionToken, calcID, docType);
            } catch (AssertionError e) {
                throw new AssertionError("Не удалось прикрепить документ: " + docType, e);
            }
        }
    }

    /**
     * Валидирует ответ API после прикрепления документа.
     *
     * @param response     ответ API
     * @param sessionToken ожидаемый токен сессии
     * @param calcID       идентификатор расчёта
     * @param typeDoc      тип документа
     * @throws AssertionError если валидация не пройдена
     */
    private static void validateResponse(AttachResponse response, String sessionToken,
                                         String calcID, String typeDoc) {
        // Проверка наличия accID
        assertNotNull(response.getAccID(),
                String.format("accID в ответе не должен быть null для документа типа %s", typeDoc));
        assertFalse(response.getAccID().isEmpty(),
                String.format("accID в ответе не должен быть пустым для документа типа %s", typeDoc));

        // Проверка соответствия sessionToken и accID
        assertEquals(sessionToken, response.getAccID(),
                String.format("sessionToken должен совпадать с accID в ответе. " +
                                "Ожидалось: %s, Получено: %s. Расчет: %s, Тип документа: %s",
                        sessionToken, response.getAccID(), calcID, typeDoc));

        // Проверка наличия docID
        assertTrue(response.getDocID() > 0,
                String.format("docID должен быть положительным числом. " +
                                "Получено: %d. Расчет: %s, Тип документа: %s",
                        response.getDocID(), calcID, typeDoc));
    }

}