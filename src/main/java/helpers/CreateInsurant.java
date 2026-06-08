package helpers;

import PDSAPI.models.*;
import com.github.javafaker.Faker;

import java.util.Date;
import java.util.Locale;

public class CreateInsurant {
    static Faker faker = new Faker(Locale.forLanguageTag("ru"));
    String INN = InnGenerator.getINNFL();
    String SNILS = SNILSGenerator.getSNILS(true);
    Date birthDate = faker.date().birthday(0, 17);

    Address residenceAddress = new Address(340063, "Пермский край, Пермский р-н, с Гамово, ул. 50 лет Октября, д. 11", "Россия", "Пермский");
    Address factAddress = new Address(614520, "614520, Россия, Пермский край, Пермский р-н, п.Кукуштан , ул. Чапаева, д. 1", "Россия", "Пермский");
    Document document = new Document("2022-06-05T12:00:00.000Z", "001-001", faker.numerify("######"), "ОВД1", faker.numerify("####"), "ПАСПОРТ_РФ");

    Physical physical = Physical.builder()
            .birthDate(DateFormatter.toCustomFormat(birthDate))
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
            .snils(SNILS)
            .document(document).build();


    public Insurant getInsurant() {
        return new Insurant(physical, "ФЛ");
    }


}
