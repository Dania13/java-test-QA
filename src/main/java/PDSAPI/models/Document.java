package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель удостоверяющего документа физического лица.
 * <p>Поддерживает различные типы документов: паспорт РФ, заграничный паспорт,
 * свидетельство о рождении, военный билет и другие.</p>
 */
@Setter
@Getter
public class Document {
    /** Дата выдачи документа в формате ISO (yyyy-MM-dd'T'HH:mm:ss.SSS'Z') */
    private String dateOfIssue;

    /** Код подразделения, выдавшего документ (например, "123-456") */
    private String kodPodrazd;

    /** Номер документа */
    private String number;

    /** Место выдачи документа */
    private String placeOfIssue;

    /** Серия документа */
    private String series;

    /** Тип документа (например, "ПАСПОРТ_РФ", "ЗАГРАН", "СВИДЕТЕЛЬСТВО_РОЖДЕНИЯ") */
    private String type;

    /**
     * Конструктор с основными параметрами документа.
     *
     * @param dateOfIssue   дата выдачи документа
     * @param kodPodrazd    код подразделения
     * @param number        номер документа
     * @param placeOfIssue  место выдачи документа
     * @param series        серия документа
     * @param type          тип документа
     */
    public Document(String dateOfIssue, String kodPodrazd, String number, String placeOfIssue, String series, String type) {
        this.dateOfIssue = dateOfIssue;
        this.kodPodrazd = kodPodrazd;
        this.number = number;
        this.placeOfIssue = placeOfIssue;
        this.series = series;
        this.type = type;
    }

    /**
     * Пустой конструктор для десериализации JSON.
     */
    public Document() {

    }
}
