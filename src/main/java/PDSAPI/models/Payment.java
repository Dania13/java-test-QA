package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель страхового платежа (взноса).
 * <p>Представляет отдельный платёж в графике страховых взносов.</p>
 */
@Getter
@Setter
public class Payment {

    /** Номер платежа по порядку */
    private int number;

    /** Дата платежа в формате ISO (yyyy-MM-dd) */
    private String date;

    /** Сумма платежа в валюте полиса */
    private double sum;

    /** Сумма платежа в рублях (для конвертации) */
    private double sumRur;
}
