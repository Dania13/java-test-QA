package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель страхователя (застрахованного лица).
 * <p>Содержит информацию о физическом или юридическом лице, на которое оформляется страховой полис.</p>
 *
 * <p>Страхователь может быть:
 * <ul>
 *   <li>Физическим лицом (тип "ФЛ")</li>
 *   <li>Юридическим лицом (тип "ЮЛ")</li>
 *   <li>Индивидуальным предпринимателем (тип "ИП")</li>
 * </ul>
 * </p>
 */
@Setter
@Getter
public class Insurant {
    /** Информация о физическом лице (заполняется для типа "ФЛ") */
    private Physical physical;

    /** Тип страхователя ("ФЛ", "ЮЛ", "ИП") */
    private String type;

    /**
     * Конструктор с основными параметрами страхователя.
     *
     * @param physical информация о физическом лице
     * @param type     тип страхователя
     */
    public Insurant(Physical physical, String type) {
        this.physical = physical;
        this.type = type;
    }

    /**
     * Пустой конструктор для десериализации JSON.
     */
    public Insurant() {

    }
}
