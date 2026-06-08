package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос аутентификации.
 * <p>Содержит результат попытки входа и токен сессии при успешной аутентификации.</p>
 */
@Setter
@Getter
public class AuthResponse {
    /** Токен сессии для последующих авторизованных запросов */
    private String sessionToken;
    /** Флаг успешности аутентификации */
    private boolean success;
    /** Сообщение с дополнительной информацией (например, текст ошибки) */
    private String message;

    /**
     * Возвращает флаг успешности аутентификации.
     *
     * @return true, если аутентификация прошла успешно, иначе false
     */
    public boolean getSuccess() { return success; }
}
