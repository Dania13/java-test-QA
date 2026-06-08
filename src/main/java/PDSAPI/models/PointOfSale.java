package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель точки продаж.
 * <p>Содержит информацию о месте оформления страхового полиса (офис, филиал, агентский пункт).</p>
 */
@Setter
@Getter
public class PointOfSale {
    /** Название точки продаж */
    private String name;

    /** Код точки продаж в системе */
    private String code;

    /** Адрес точки продаж */
    private Address address;
}