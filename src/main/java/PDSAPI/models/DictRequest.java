package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на получение данных из справочника (словаря).
 * <p>Используется для получения справочных значений, таких как списки кодов, классификаторов,
 * возможных значений параметров и т.д.</p>
 */
@Setter
@Getter
@Builder
public class DictRequest {
    /** Индентификатор сессии */
    private String accID;

    /** Код страхового продукта, для которого запрашивается справочник */
    private String product;

    /** Код запрашиваемого справочника (словаря) */
    private String dictionaryCode;
}
