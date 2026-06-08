package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на аннулирование полиса.
 * <p>Используется для отправки запроса на отмену ранее оформленного договора страхования.</p>
 */
@Setter
@Getter
@Builder
public class AnnulRequest {
    /** Идентификатор расчёта, который необходимо аннулировать */
    private String calcID;

    /** Причина аннулирования */
    private String reason;
}
