package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на печать документа по полису.
 * <p>Используется для генерации печатных форм страхового полиса, квитанций и других документов.</p>
 */
@Setter
@Getter
@Builder
public class PrintRequest {
    /** Идентификатор расчёта */
    private String calcID;

    /** Тип печатной формы (например, "Печать" для печати оформленного полиса, "Черновик" для печати черновика) */
    private String type;
}