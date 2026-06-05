package PDSAPI.actions;

import PDSAPI.models.*;
import com.github.javafaker.Faker;
import helpers.DateFormatter;
import helpers.InnGenerator;
import helpers.SNILSGenerator;

import java.util.Locale;

public class CreateInsurant {
    static Faker faker = new Faker(Locale.forLanguageTag("ru"));
    String INN = InnGenerator.getINNFL();
    String SNILS = SNILSGenerator.getSNILS(true);

    Address residenceAddress = new Address(340063, "Пермский край, Пермский р-н, с Гамово, ул. 50 лет Октября, д. 11", "Россия", "Пермский");
    Address factAddress = new Address(614520, "614520, Россия, Пермский край, Пермский р-н, п.Кукуштан , ул. Чапаева, д. 1", "Россия", "Пермский");
//        Document document = new Document("2020-01-01T12:00:00.000Z", "001-001", "001011", "ОВД1", "0101", "ПАСПОРТ_РФ");

    Physical physical = Physical.builder()
            .birthDate(DateFormatter.toCustomFormat(faker.date().birthday(18, 120)))
            .birthplace("Гор. Лермонтов")
            .citizenship("Россия")
            .email("qa@virtusystems.ru")
            .factAddress(factAddress)
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .middleName("Ивановна")
            .isIP("false")
            .phone("+79997778866")
            .residenceAddress(residenceAddress)
            .sex("F")
            .inn(INN)
            .snils(SNILS).build();


    public Insurant getInsurant() {
        return new Insurant(physical, "ФЛ");
    }


}
