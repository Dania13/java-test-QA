package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на импорт страхового полиса.
 * <p>Используется для сохранения с расчётом полиса.</p>
 */
@Setter
@Getter
@Builder
public class ImportRequest {
    /**
     * Объект полиса со всеми данными
     */
    private PolicyImport policy;
}


