package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель отдельной ошибки в ответе API.
 * <p>Содержит детальную информацию об ошибке, возникшей при обработке запроса.
 * Используется для стандартизированного возврата ошибок клиенту.</p>
 *
 * <p>Пример ответа с ошибкой:</p>
 * <pre>
 * {
 *   "message": "Validation failed",
 *   "type": "VALIDATION_ERROR",
 *   "detailMessage": "Field 'inn' has invalid format"
 * }
 * </pre>
 */
@Setter
@Getter
public class Error {
    /** Краткое описание ошибки (человекочитаемое) */
    private String message;

    /** Тип ошибки для программной обработки (например, "VALIDATION_ERROR", "AUTH_ERROR") */
    private String type;

    /** Детализированное сообщение с дополнительной информацией об ошибке */
    private String detailMessage;
}
