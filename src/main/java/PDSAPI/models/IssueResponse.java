package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос оформления страхового полиса.
 * <p>Содержит результат операции выпуска полиса, включая идентификаторы созданных объектов
 * и возможные ошибки при оформлении.</p>
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class IssueResponse {
    /** Индентификатор сессии */
    private String accID;

    /** Идентификатор оформленного полиса */
    private String policyID;

    /** Ошибки, возникшие при оформлении полиса (при наличии) */
    private Errors errors;

    /**
     * Пустой конструктор для десериализации JSON.
     */
    public IssueResponse(){

    }
}
