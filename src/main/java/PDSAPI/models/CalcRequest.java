package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на расчёт стоимости страхового полиса.
 * <p>Содержит тип продукта и параметры расчёта для получения предварительной стоимости страхования.</p>
 */
@Builder
@Setter
@Getter
public class CalcRequest {
    /** Тип страхового продукта (например, "ПДС", "НСЖ", "ИСЖ") */
    private String productType;
    /** Параметры расчёта полиса */
    private PolicyCalc policyCalc;
}


