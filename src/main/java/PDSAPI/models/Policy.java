package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Основная модель страхового полиса.
 * <p>Содержит полную информацию о страховом полисе: страховщика, страхователя,
 * продукт, риски, платежи, параметры и т.д.</p>
 */
@Setter
@Getter
public class Policy {
    /** Название страховой компании */
    private String insCompanyName;

    /** Страховой продукт */
    private Product product;

    /** Номер полиса */
    private String number;

    /** Дата оформления полиса */
    private String date;

    /** Статус полиса (оформлен, аннулирован, проект) */
    private String state;

    /** Признак пролонгации (автоматического продления) */
    private Boolean prolongation;

    /** Страхователь */
    private Insurant insurant;

    /** Тип документа страхователя */
    private String insurantDocType;

    /** Объекты страхования */
    private InsuranceObjects insuranceObjects;

    /** Общая сумма страховой премии */
    private Double insPremTotal;

    /** Комментарий к полису */
    private String comment;

    /** Представитель (при наличии) */
    private Representative representative;

    /** Список курсов валют */
    private CurrExchList currExchList;

    /** Идентификатор полиса в системе */
    @JsonProperty("ID")
    private String ID;

    /** Информация о договоре дилера */
    private DealerConractInfo dealerConractInfo;

    /** Точка продаж */
    private PointOfSale pointOfSale;

    /** Информация о пользователе */
    private UserInfo userInfo;

    /** Код валюты полиса (например, "RUR") */
    private String currCode;

    /** Идентификатор расчёта */
    private String calcID;

    /** График платежей */
    private PaymentsPlan paymentsPlan;

    /** Скидка по АВ (автовладельцам) */
    private Double discountAV;

    /** Скидка андеррайтера */
    private Double discountUnderwriter;

    /** Коэффициент КСП */
    private Double ksp;

    /** Клиент без убытков */
    private Boolean clientWithoutLoss;

    /** Признак мошенничества в страховании */
    private Boolean foulOfInsurance;

    /** Дата расчёта */
    private String dateCalc;

    /** Внутренний идентификатор */
    private Integer internalID;

    /** Флаг Печать на бланке */
    private Boolean pechatNaBlanke;

    /** Скидка АВ в процентах */
    private Boolean skidkaAvVProc;

    /** Параметры полиса */
    private Parameters parameters;

    /** Флаг Электронный полис */
    private Boolean epolicy;

    /** Флаг Онлайн-оплата */
    private Boolean onlinePayment;

    /** Пустой конструктор для десериализации JSON */
    public Policy() {
    }
}