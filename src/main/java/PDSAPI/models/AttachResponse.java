package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос прикрепления файла.
 * <p>Содержит идентификаторы созданного документа и токена авторизации</p>
 */
@Setter
@Getter
public class AttachResponse {
    /** Индентификатор сессии */
    private String accID;

    /** Идентификатор созданного документа в системе */
    private Integer docID;

    /** Объект с информацией об ошибках */
    private Errors errors;
}
