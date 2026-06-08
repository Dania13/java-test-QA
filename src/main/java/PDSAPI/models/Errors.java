package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Контейнер для списка ошибок в ответе API.
 * <p>Используется для возврата множества ошибок в одном ответе,
 * например, при валидации сложных объектов с несколькими некорректными полями.</p>
 *
 * <p>Пример ответа с несколькими ошибками:</p>
 * <pre>
 * {
 *   "errors": [
 *     {
 *       "message": "Field required",
 *       "type": "REQUIRED_FIELD",
 *       "detailMessage": "Field 'firstName' is required"
 *     },
 *     {
 *       "message": "Invalid format",
 *       "type": "VALIDATION_ERROR",
 *       "detailMessage": "Field 'email' has invalid format"
 *     }
 *   ]
 * }
 * </pre>
 */
@Setter
@Getter
public class Errors {
    /** Список ошибок, возникших при обработке запроса */
    private List<Error> errors;

    /**
     * Пустой конструктор для десериализации JSON.
     */
    public Errors() {
    }

    /**
     * Конструктор с инициализацией списка ошибок.
     *
     * @param errors список ошибок
     */
    public Errors(List<Error> errors) {
        this.errors = errors;
    }
}