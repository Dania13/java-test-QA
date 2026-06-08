package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель дилера в системе страхования.
 * <p>Представляет дилера (посредника), который может быть как физическим, так и юридическим лицом.
 * Дилеры участвуют в продаже страховых продуктов через свои каналы.</p>
 */
@Setter
@Getter
public class Dealer {
    /** Тип дилера ("ФЛ" - физическое лицо, "ЮЛ" - юридическое лицо) */
    private String type;

    /** Юридическое лицо (заполняется, если type = "ЮЛ") */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Juridical juridical;
}
