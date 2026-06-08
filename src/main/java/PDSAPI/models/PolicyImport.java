package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Данные для импорта страхового полиса.
 */
@Setter
@Getter
@Builder
public class PolicyImport {

    /** Код валюты полиса */
    private String currCode;

    /** Название страховой компании */
    private String insCompanyName;

    /** Комментарий к полису */
    private String comment;

    /** Страхователь */
    private Insurant insurant;

    /** Объекты страхования */
    private InsuranceObjects insuranceObjects;

    /** Параметры полиса */
    private Parameters parameters;

    /** Страховой продукт */
    private Product product;
}