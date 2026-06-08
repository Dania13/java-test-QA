package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Результат расчёта страхового полиса.
 * <p>Содержит список результатов расчёта</p>
 */
@Setter
@Getter
public class CalcPolicyResult {
    /** Список результатов расчёта полиса */
    private List<CalcResults> calcResults;
}
