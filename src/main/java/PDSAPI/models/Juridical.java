package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель юридического лица.
 * <p>Содержит базовую информацию о юридическом лице, участвующем в страховании.</p>
 */
@Setter
@Getter
public class Juridical {
    /** Внутренний идентификатор в системе */
    private int internalID;

    /** Полное наименование юридического лица */
    private String fullName;

    /** Краткое наименование юридического лица */
    private String shortName;
}
