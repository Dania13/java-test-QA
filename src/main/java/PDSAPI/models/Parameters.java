package PDSAPI.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Контейнер для списка параметров полиса.
 * <p>Содержит коллекцию параметров, используемых для настройки страхового продукта.</p>
 */
@Setter
@Getter
public class Parameters {
    /** Список параметров */
    private List<Parameter> parameters;

    /**
     * Конструктор с инициализацией списка параметров.
     *
     * @param parameters список параметров
     */
    public Parameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /** Пустой конструктор для десериализации JSON */
    public Parameters() {
    }
}
