package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель физического лица (страхователя).
 * <p>Содержит полную информацию о физическом лице: ФИО, дату рождения,
 * паспортные данные, контакты, адреса, ИНН, СНИЛС и т.д.</p>
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Physical {

    /** Внутренний идентификатор в системе */
    private int internalID;

    /** Имя на латинице (для загранпаспорта) */
    private String firstNameLat;

    /** Фамилия на латинице (для загранпаспорта) */
    private String lastNameLat;

    /** Отчество на латинице (для загранпаспорта) */
    private String middleNameLat;

    /** Дата рождения в формате ISO */
    private String birthDate;

    /** Место рождения */
    private String birthplace;

    /** Гражданство */
    private String citizenship;

    /** Паспортные данные */
    private Document passport;

    /** Документ, удостоверяющий личность */
    private Document document;

    /** Адрес электронной почты */
    private String email;

    /** Фактический адрес проживания */
    private Address factAddress;

    /** Почтовый адрес */
    private Address postAddress;

    /** Имя */
    private String firstName;

    /** Фамилия */
    private String lastName;

    /** Отчество */
    private String middleName;

    /** Является ли индивидуальным предпринимателем ("true"/"false") */
    private String isIP;

    /** Номер телефона */
    private String phone;

    /** Адрес регистрации (прописки) */
    private Address residenceAddress;

    /** Пол ("M" - мужской, "F" - женский) */
    private String sex;

    /** Номер СНИЛС */
    private String snils;

    /** ИНН (12 цифр для физического лица) */
    private String inn;

    /** Список документов */
    private Documents documents;

    /** Пустой конструктор для десериализации JSON */
    public Physical() {
    }
}