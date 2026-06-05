package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {
    private String country;
    private String countryCodeOKSM;
    private String region;
    private String locality;
    private String localityCodeKLADR;
    private String addressText;
}
