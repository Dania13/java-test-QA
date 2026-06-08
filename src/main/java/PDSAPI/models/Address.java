package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель адреса для страхования.
 * <p>Содержит информацию о почтовом адресе, включая страну, регион, населенный пункт и почтовый индекс.</p>
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Address {
    /** Название страны */
    private String country;

    /** Код страны по классификатору ОКСМ */
    private String countryCodeOKSM;

    /** Регион (область, край, республика) */
    private String region;

    /** Населенный пункт */
    private String locality;

    /** Код населенного пункта по КЛАДР */
    private String localityCodeKLADR;

    /** Текстовое представление адреса */
    private String addressText;

    /** Почтовый индекс */
    private Integer postIndex;

    /**
     * Конструктор для создания адреса с основными полями.
     *
     * @param postIndex   почтовый индекс
     * @param addressText текстовое представление адреса
     * @param country     название страны
     * @param region      название региона
     */
    public Address(int postIndex, String addressText, String country, String region) {
        this.postIndex = postIndex;
        this.addressText = addressText;
        this.country = country;
        this.region = region;
    }

    /** Пустой конструктор для десериализации JSON */
    public Address() {

    }
}
