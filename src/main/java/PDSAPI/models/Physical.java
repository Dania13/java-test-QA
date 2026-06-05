package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
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

    public Physical() {

    }
}
