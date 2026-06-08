package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель страхового продукта.
 * <p>Представляет конкретный страховой продукт, предлагаемый страховой компанией.
 * Например: "Программа долгосрочных сбережений граждан (ПДС)", "АвтоКАСКО", "Ипотечное страхование" и т.д.</p>
 */
@Setter
@Getter
public class Product {

    /** Название страхового продукта */
    private String name;

    /**
     * Конструктор с названием продукта.
     *
     * @param name название продукта
     */
    public Product(String name) {
        this.name = name;
    }

    /** Пустой конструктор для десериализации JSON */
    public Product() {
    }
}