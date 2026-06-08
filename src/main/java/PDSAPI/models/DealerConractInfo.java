package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Информация о договоре с дилером.
 * <p>Содержит тип договора и информацию об агенте и дилере, участвующих в сделке.</p>
 */
@Setter
@Getter
public class DealerConractInfo {
    /** Тип договора или сделки (например, АГЕНТСКИЙ_ДОГОВОР)*/
    private String type;

    /** Информация об агентском договоре */
    private AgentConract agentConract;

    /** Информация о дилере */
    private Dealer dealer;
}
