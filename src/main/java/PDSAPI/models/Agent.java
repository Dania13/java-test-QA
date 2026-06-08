package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель агента страхования.
 * <p>Представляет страхового агента, который может быть физическим или юридическим лицом.</p>
 */
@Setter
@Getter
public class Agent {

    /** Тип агента (например, "ФЛ" - физическое лицо, "ЮЛ" - юридическое лицо) */
    private String type;

    /** Юридическое лицо (заполняется, если type = "ЮЛ") */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Juridical juridical;
}
