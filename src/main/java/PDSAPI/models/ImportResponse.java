package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос импорта страхового полиса.
 * <p>Содержит результат импорта: созданный полис, а также возможные ошибки и предупреждения.</p>
 */
@Setter
@Getter
public class ImportResponse {
    /** Созданный объект полиса (при успешном импорте) */
    private Policy policy;

    /** Список ошибок, возникших при импорте (при наличии) */
    private Errors errors;

    /** Список предупреждений, возникших при импорте (не критичные замечания) */
    private Warnings warnings;

}


