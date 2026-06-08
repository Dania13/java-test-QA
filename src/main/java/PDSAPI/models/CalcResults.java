package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Результат расчёта для конкретного варианта полиса.
 * <p>Содержит объект полиса с вычисленными параметрами и стоимостью.</p>
 */
@Setter
@Getter
public class CalcResults {
    /** Рассчитанный объект полиса со всеми параметрами */
    private Policy policy;
}
