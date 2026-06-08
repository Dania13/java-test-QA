package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос печати документа.
 * <p>Содержит URL для скачивания сгенерированного документа или ошибку при возникновении проблем.</p>
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PrintResponse {
    /** Номер сгенерированного документа */
    private String number;

    /** URL для скачивания документа */
    private String url;

    /** Ошибки при генерации документа */
    private Errors errors;
}