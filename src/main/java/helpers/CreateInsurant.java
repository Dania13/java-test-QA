package helpers;

import PDSAPI.models.*;
import com.github.javafaker.Faker;

import java.util.Date;
import java.util.Locale;

/**
 * Утилитарный класс для создания объекта страхователя (Insurant) со сгенерированными тестовыми данными.
 * <p>
 * Использует библиотеку Faker для генерации случайных персональных данных, а также вспомогательные
 * генераторы для создания валидных ИНН и СНИЛС физического лица.
 * </p>
 */
public class CreateInsurant {
    /** Генератор случайных данных с русской локализацией */
    static Faker faker = new Faker(Locale.forLanguageTag("ru"));

    String middle = faker.name().nameWithMiddle();
    String[] middleArray = middle.split(" ");

    /** Генерирует валидный ИНН физического лица (12 цифр) */
    String INN = InnGenerator.getINNFL();

    /** Генерирует валидный СНИЛС в форматированном виде (XXX-XXX-XXX XX) */
    String SNILS = SNILSGenerator.getSNILS(true);

    /** Случайная дата рождения (возраст от 18 до 120 лет) */
    Date birthDate = faker.date().birthday(18, 120);

    /** Адрес регистрации (постоянная прописка) */
    Address residenceAddress = new Address(340063, "Пермский край, Пермский р-н, с Гамово, ул. 50 лет Октября, д. 11", "Россия", "Пермский");

    /** Фактический адрес проживания */
    Address factAddress = new Address(614520, "614520, Россия, Пермский край, Пермский р-н, п.Кукуштан , ул. Чапаева, д. 1", "Россия", "Пермский");

    /** Документ, удостоверящий личность */
    Document document = new Document("2022-06-05T12:00:00.000Z", "001-001", faker.numerify("######"), "ОВД1", faker.numerify("####"), "ПАСПОРТ_РФ");

    /**
     * Физическое лицо (страхователь) со всеми сгенерированными данными.
     * <p>Включает: ФИО, дату и место рождения, гражданство, контакты, адреса,
     * пол, ИНН, СНИЛС и паспортные данные.</p>
     */
    Physical physical = Physical.builder()
            .birthDate(DateFormatter.toCustomFormat(birthDate))
            .birthplace("Гор. Лермонтов")
            .citizenship("Россия")
            .email("qa@virtusystems.ru")
            .factAddress(factAddress)
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .middleName(middleArray[1])
            .isIP("false")
            .phone("+79997778866")
            .residenceAddress(residenceAddress)
            .sex("F")
            .inn(INN)
            .snils(SNILS)
            .document(document).build();

    /**
     * Возвращает готовый объект страхователя.
     *
     * @return объект {@link Insurant} с типом "ФЛ" (физическое лицо)
     */
    public Insurant getInsurant() {
        return new Insurant(physical, "ФЛ");
    }


}
