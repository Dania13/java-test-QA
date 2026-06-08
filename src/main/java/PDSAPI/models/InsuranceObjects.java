package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Контейнер для списка объектов страхования.
 * <p>Содержит перечень объектов, которые страхуются по полису.
 * Например, при страховании жизни объектом может быть жизнь и здоровье застрахованного лица,
 * при имущественном страховании - конкретное имущество.</p>
 */
@Setter
@Getter
public class InsuranceObjects {
    /** Список объектов страхования */
    private List<InsuranceObject> objects;

    /**
     * Конструктор с инициализацией списка объектов страхования.
     *
     * @param objects список объектов страхования
     */
    public InsuranceObjects(List<InsuranceObject> objects) {
        this.objects = objects;
    }

    /**
     * Пустой конструктор для десериализации JSON.
     */
    public InsuranceObjects() {
    }
}