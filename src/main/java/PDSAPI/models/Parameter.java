package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Модель параметра для настройки страхового полиса.
 * <p>Представляет отдельный параметр с указанием кода, названия, типа и значения.
 * Поддерживает различные типы значений: строковые, целочисленные, вещественные и логические.</p>
 */
@Setter
@Getter
public class Parameter {
    /** Код параметра (например, "dogovor.predvRaschet") */
    private String code;

    /** Название параметра (человекочитаемое) */
    private String name;

    /** Строковое значение параметра */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String stringValue;

    /** Тип параметра ("Логический", "Строковый", "Числовой", "Вещественный") */
    private String type;

    /** Целочисленное значение параметра */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer intValue;

    /** Вещественное значение параметра */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double decimalValue;

    /** Логическое значение параметра */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean boolValue;

    /** Значение в формате дата параметра */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dateValue;

    /**
     * Конструктор для строкового параметра.
     *
     * @param code        код параметра
     * @param name        название параметра
     * @param stringValue строковое значение
     * @param type        тип параметра
     */
    public Parameter (String code, String name, String stringValue, String type) {
        this.code = code;
        this.name = name;
        this.stringValue = stringValue;
        this.type = type;
    }

    /**
     * Конструктор для целочисленного параметра.
     *
     * @param code     код параметра
     * @param name     название параметра
     * @param intValue целочисленное значение
     * @param type     тип параметра
     */
    public Parameter (String code, String name, int intValue, String type) {
        this.code = code;
        this.name = name;
        this.intValue = intValue;
        this.type = type;
    }

    /**
     * Конструктор для логического параметра.
     *
     * @param code      код параметра
     * @param name      название параметра
     * @param boolValue логическое значение
     * @param type      тип параметра
     */
    public Parameter (String code, String name, boolean boolValue, String type) {
        this.code = code;
        this.name = name;
        this.boolValue = boolValue;
        this.type = type;
    }

    /**
     * Конструктор для вещественного параметра.
     *
     * @param code         код параметра
     * @param name         название параметра
     * @param decimalValue вещественное значение
     * @param type         тип параметра
     */
    public Parameter (String code, String name, double decimalValue, String type) {
        this.code = code;
        this.name = name;
        this.decimalValue = decimalValue;
        this.type = type;
    }

    /**
     * Конструктор для параметра - даты.
     *
     * @param code         код параметра
     * @param name         название параметра
     * @param dateValue вещественное значение
     * @param type         тип параметра
     */
    public Parameter (String code, String name, Instant dateValue, String type) {
        this.code = code;
        this.name = name;
        this.dateValue = dateValue;
        this.type = type;
    }

    /** Пустой конструктор для десериализации JSON */
    public Parameter () {
    }

}