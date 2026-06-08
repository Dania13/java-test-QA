package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель объекта страхования.
 * <p>Представляет конкретный объект, который страхуется по полису (например,
 * жизнь и здоровье человека, автомобиль, недвижимость и т.д.).</p>
 */
@Getter
@Setter
public class InsuranceObject {
    /** Название объекта страхования */
    private String name;

    /** Дополнительные параметры объекта страхования (зависят от типа объекта) */
    private java.lang.Object parameters;

    /** Информация о рисках, связанных с объектом страхования */
    private RiskInfo riskInfo;

    /**
     * Конструктор с основными параметрами.
     *
     * @param name       название объекта страхования
     * @param parameters дополнительные параметры
     * @param riskInfo   информация о рисках
     */
    public InsuranceObject(String name, java.lang.Object parameters, RiskInfo riskInfo) {
        this.name = name;
        this.parameters = parameters;
        this.riskInfo = riskInfo;
    }

    /** Пустой конструктор для десериализации JSON */
    public InsuranceObject() {
    }

}