package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель страхового риска.
 * <p>Представляет конкретный страховой риск, покрываемый полисом,
 * с указанием стоимости страховой премии.</p>
 */
@Getter
@Setter
public class Risk {

    /** Признак застрахованности ("true"/"false") */
    private String insured;

    /** Название риска (например, "Пенсионное накопление", "Дожитие", "Смерть") */
    private String name;

    /** Сумма страховой премии по риску */
    private Integer insPrem;

    /** Франшиза по риску (при наличии) */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Double fransiza;

    /** Коэффициенты по риску */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Koefficients koefficients;

    /**
     * Конструктор для создания риска.
     *
     * @param insured признак застрахованности
     * @param name    название риска
     * @param insPrem сумма премии
     */
    public Risk(String insured, String name, int insPrem) {
        this.insured = insured;
        this.name = name;
        this.insPrem = insPrem;
    }

    /** Пустой конструктор для десериализации JSON */
    public Risk() {
    }
}