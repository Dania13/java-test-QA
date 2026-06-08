package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Контейнер для списка страховых рисков.
 * <p>Содержит коллекцию рисков, включённых в страховой полис.</p>
 */
@Setter
@Getter
public class RiskInfo {
    /** Список страховых рисков */
    private List<Risk> risks;

    /**
     * Конструктор с инициализацией списка рисков.
     *
     * @param risks список рисков
     */
    public RiskInfo(List<Risk> risks) {
        this.risks = risks;
    }

    /** Пустой конструктор для десериализации JSON */
    public RiskInfo() {
    }
}