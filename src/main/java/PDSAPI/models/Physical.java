package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Physical {

    private int internalID;
    private String firstNameLat;
    private String lastNameLat;
    private String middleNameLat;
    private String birthDate;
    private String birthplace;
    private String citizenship;
    private Document passport;
    private Document document;
    private String email;
    private Address factAddress;
    private Address postAddress;
    private String firstName;
    private String lastName;
    private String middleName;
    private String isIP;
    private String phone;
    private Address residenceAddress;
    private String sex;
    private String snils;
    private String inn;
    private Documents documents;

    public Physical(String birthDate, String birthplace, String citizenship, Document document, String email, Address factAddress, String firstName, String lastName, String middleName, String isIP, String phone, Address residenceAddress, String sex, String snils, String inn) {
        this.birthDate = birthDate;
        this.birthplace = birthplace;
        this.citizenship = citizenship;
        this.document = document;
        this.email = email;
        this.factAddress = factAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.isIP = isIP;
        this.phone = phone;
        this.residenceAddress = residenceAddress;
        this.sex = sex;
        this.snils = snils;
        this.inn = inn;
    }

    public Physical() {

    }
}
