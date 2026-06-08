package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на прикрепление файла к полису.
 * <p>Используется для загрузки и привязки документов (например, сканов паспорта, анкеты) к полису.</p>
 */
@Setter
@Getter
@Builder
public class AttachRequest {
    /** Идентификатор расчёта, к которому прикрепляется файл */
    private String calcID;

    /** Имя файла (с расширением) */
    private String fileName;

    /** Тип документа (например, "Документ, удостоверящий личность") */
    private String type;

    /** Комментарий к прикрепляемому файлу */
    private String comment;

    /** Содержимое файла в формате Base64 */
    private String attachment;
}
