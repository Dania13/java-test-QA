package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Список курсов валют.
 * <p>Модель для хранения информации о курсах обмена валют.
 * В текущей версии не содержит полей, требуется уточнение структуры данных.</p>
 *
 * <p><b>TODO:</b> Добавить поля согласно спецификации API:
 * <ul>
 *   <li>курс валюты</li>
 *   <li>дата курса</li>
 *   <li>код валюты</li>
 * </ul>
 * </p>
 */
@Setter
@Getter
public class CurrExchList {
    // TODO: Добавить поля согласно документации API
    // Пример возможных полей:
    // private String currencyCode;
    // private BigDecimal exchangeRate;
    // private LocalDate rateDate;
}
