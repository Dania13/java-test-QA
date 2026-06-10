package PDSAPI.actions;

import PDSAPI.models.AuthRequest;
import PDSAPI.models.AuthResponse;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для выполнения аутентификации пользователя в системе страхования.
 * <p>Предоставляет методы для авторизации пользователей с кэшированием токенов
 * сессии для оптимизации последующих запросов.</p>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Кэширование токенов сессии для повторного использования</li>
 *   <li>Автоматическое обновление истёкших токенов</li>
 *   <li>Валидация учётных данных перед отправкой запроса</li>
 *   <li>Подробное логирование всех операций</li>
 * </ul>
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * // Первый вызов - выполняет авторизацию
 * String token = Auth.getCachedSessionToken("user", "pass");
 *
 * // Второй вызов в течение 55 минут - использует кэш
 * String cachedToken = Auth.getCachedSessionToken("user", "pass");
 *
 * // Принудительная авторизация без кэша
 * String newToken = Auth.loginUser("user", "pass");
 * </pre>
 */
public final class Auth {

    /** Кэшированный токен сессии */
    private static String cachedSessionToken;

    /** Логин пользователя, для которого закэширован токен */
    private static String cachedUserLogin;

    /** Время истечения действия кэшированного токена */
    private static Instant tokenExpiryTime;

    /** Логгер для записи событий аутентификации */
    private static final Logger log = LoggerFactory.getLogger(Auth.class);

    /** Время жизни токена в минутах (55 минут из 60 для запаса) */
    private static final long TOKEN_LIFETIME_MINUTES = 55;

    /**
     * Приватный конструктор для утилитарного класса.
     * Запрещает создание экземпляров класса.
     */
    private Auth() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Возвращает токен сессии, используя кэш если он еще действителен.
     * <p>При первом вызове или при истечении срока действия токена
     * выполняет авторизацию и кэширует новый токен.</p>
     *
     * <p><b>Время жизни токена:</b> 55 минут (токен в API обычно живёт 1 час,
     * используется запас в 5 минут для безопасности).</p>
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return токен сессии (из кэша или новый)
     * @throws IllegalArgumentException если login или password пустые
     * @throws AssertionError если авторизация не успешна
     */
    @Step("Получение токена сессии (с кэшированием) для пользователя: {login}")
    public static String getCachedSessionToken(String login, String password) {
        log.debug("Запрос токена сессии для пользователя: {}", login);

        if (isTokenValid(login)) {
            log.debug("Используем кэшированный токен для пользователя: {}", login);
            return cachedSessionToken;
        }

        log.info("Кэш токена недействителен или отсутствует, выполняем авторизацию для: {}", login);
        String newToken = loginUser(login, password);
        cacheToken(login, newToken);
        return newToken;
    }


    /**
     * Проверяет, действителен ли кэшированный токен.
     *
     * @param login логин пользователя для проверки соответствия
     * @return true, если токен существует, соответствует пользователю и не истёк
     */
    private static boolean isTokenValid(String login) {
        boolean hasToken = cachedSessionToken != null;
        boolean hasUser = cachedUserLogin != null;
        boolean userMatches = hasUser && cachedUserLogin.equals(login);
        boolean notExpired = tokenExpiryTime != null && Instant.now().isBefore(tokenExpiryTime);

        log.trace("Проверка валидности токена: token={}, user={}, userMatches={}, notExpired={}",
                hasToken, hasUser, userMatches, notExpired);

        return hasToken && userMatches && notExpired;
    }

    /**
     * Сохраняет токен в кэш с установкой времени истечения.
     *
     * @param login логин пользователя
     * @param token токен сессии
     */
    private static void cacheToken(String login, String token) {
        cachedSessionToken = token;
        cachedUserLogin = login;
        tokenExpiryTime = Instant.now().plus(Duration.ofMinutes(TOKEN_LIFETIME_MINUTES));
        log.debug("Токен кэширован для пользователя: {} до: {}", login, tokenExpiryTime);
    }

    /**
     * Выполняет авторизацию пользователя с получением нового токена сессии.
     * <p>Метод отправляет POST-запрос на эндпоинт аутентификации,
     * проверяет успешность авторизации и возвращает полученный токен.</p>
     *
     * <p><b>Шаги проверки:</b>
     * <ol>
     *   <li>Валидация учётных данных</li>
     *   <li>Проверка HTTP статуса 200 OK</li>
     *   <li>Проверка наличия sessionToken в ответе</li>
     *   <li>Проверка успешности аутентификации через флаг success</li>
     * </ol>
     * </p>
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return токен сессии (sessionToken) для использования в последующих запросах
     * @throws IllegalArgumentException если login или password пустые
     * @throws AssertionError если авторизация не успешна
     */
    @Step("Выполнение авторизации пользователя")
    public static String loginUser(String login, String password) {
        validateCredentials(login, password);
        log.info("Авторизация пользователя");

        AuthResponse response = executeAuthRequest(login, password);
        validateAuthResponse(response);

        log.info("Авторизация успешна для пользователя");
        return response.getSessionToken();
    }

    /**
     * Валидирует учетные данные перед отправкой запроса.
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @throws IllegalArgumentException если данные не валидны
     */
    private static void validateCredentials(String login, String password) {
        assertNotNull(login, "Логин не должен быть null");
        assertNotNull(password, "Пароль не должен быть null");
        assertFalse(login.trim().isEmpty(), "Логин не должен быть пустым");
        assertFalse(password.trim().isEmpty(), "Пароль не должен быть пустым");

        if (login.length() > 100) {
            log.warn("Логин имеет необычно большую длину: {} символов", login.length());
        }
    }

    /**
     * Выполняет HTTP-запрос к API аутентификации.
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return объект {@link AuthResponse} с ответом от сервера
     */
    private static AuthResponse executeAuthRequest(String login, String password) {
        AuthRequest request = AuthRequest.builder()
                .login(login)
                .password(password)
                .build();

        log.debug("Отправка запроса аутентификации на эндпоинт: {}", ConstantValues.AUTH_ENDPOINT);

        return given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(request)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.AUTH_ENDPOINT)
        .then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class)
                ;
    }

    /**
     * Валидирует ответ аутентификации от сервера.
     * <p>Проверяет успешность операции и наличие токена сессии.</p>
     *
     * @param response объект {@link AuthResponse} для валидации
     * @throws AssertionError если ответ не соответствует ожиданиям
     */
    private static void validateAuthResponse(AuthResponse response) {
        assertNotNull(response, "Ответ не должен быть null");

        // Проверка успешности авторизации
        if (!response.getSuccess()) {
            String errorMsg = String.format(
                    "Авторизация не успешна. Success: %s, Message: %s",
                    response.getSuccess(),
                    response.getMessage() != null ? response.getMessage() : "нет сообщения"
            );
            log.error(errorMsg);
            throw new AssertionError(errorMsg);
        }

        // Проверка наличия токена сессии
        assertNotNull(response.getSessionToken(),
                "При успешной авторизации sessionToken не должен быть null");
        assertFalse(response.getSessionToken().isEmpty(),
                "При успешной авторизации sessionToken не должен быть пустым");

        // Проверка минимальной длины токена (обычно JWT или UUID)
        if (response.getSessionToken().length() != 36) {
            log.warn("sessionToken не равно стандартным 36 символам, а имеет {}",
                    response.getSessionToken().length());
        }
    }

}