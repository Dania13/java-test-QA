package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель агентского договора.
 * <p>Содержит информацию о договоре между страховой компанией и агентом.</p>
 */
@Setter
@Getter
public class AgentConract {

    /** Информация об агенте */
    private Agent agent;

    /** Номер договора */
    private String number;

    /** Дата заключения договора в формате ISO (yyyy-MM-dd'T'HH:mm:ss.SSS'Z') */
    private String date;

    /** Код подразделения */
    private String departmentCode;

    /** ИКП (Идентификационный код предприятия) */
    private String ikp;

    /** Канал продаж */
    private String salesChannel;
}
