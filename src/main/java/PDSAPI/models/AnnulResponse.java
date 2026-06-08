package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос аннулирования.
 * <p>Содержит результат выполнения операции аннулирования полиса.</p>
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AnnulResponse {
    /** Индентификатор сессии */
    private String accID;

    /** Идентификатор аннулированного расчёта */
    private String calcID;

    /** Флаг успешности выполнения операции */
    private boolean ok;

    /** Объект с информацией об ошибках (заполняется при ok = false) */
    private Errors errors;
}
