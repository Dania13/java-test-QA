package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Параметры для расчёта страхового полиса.
 * <p>Используется для предварительного расчёта стоимости страхования
 * на основе выбранных продуктов, объектов и параметров.</p>
 */
@Setter
@Getter
@Builder
public class PolicyCalc {

    /** Код валюты расчёта */
    private String currCode;

    /** Название страховой компании */
    private String insCompanyName;

    /** Объекты страхования */
    private InsuranceObjects insuranceObjects;

    /** Признак онлайн-оплаты */
    private boolean onlinePayment;

    /** Параметры расчёта */
    private Parameters parameters;

    /** Страховой продукт */
    private Product product;
}