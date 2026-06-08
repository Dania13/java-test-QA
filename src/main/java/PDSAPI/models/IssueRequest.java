package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на оформление (выпуск) страхового полиса.
 * <p>Используется для финального оформления полиса после успешного расчета и проверки всех данных.</p>
 */
@Setter
@Getter
@Builder
public class IssueRequest {
    /** Идентификатор полиса, который необходимо оформить */
    private String policyID;
}
