package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос расчёта страхового полиса.
 * <p>Содержит идентификатор сессии и результаты расчёта полиса.</p>
 */
@Setter
@Getter
public class CalcResponse {
    /** Индентификатор сессии */
    private String accID;
    /** Результаты расчёта полиса */
    private CalcPolicyResult calcPolicyResult;
}
