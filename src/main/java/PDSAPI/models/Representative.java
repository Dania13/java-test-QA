package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель представителя страхователя.
 * <p>Содержит информацию о лице, представляющем интересы страхователя,
 * обычно по доверенности.</p>
 */
@Getter
@Setter
public class Representative {

    /** Фамилия представителя */
    public String lastName;

    /** Номер доверенности */
    public String procuracyNumber;

    /** Дата выдачи доверенности */
    public String procuracyDate;
}