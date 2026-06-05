package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Address {
    private String country;
    private String countryCodeOKSM;
    private String region;
    private String locality;
    private String localityCodeKLADR;
    private String addressText;
    private Integer postIndex;

    public Address(int postIndex, String addressText, String country, String region) {
        this.postIndex = postIndex;
        this.addressText = addressText;
        this.country = country;
        this.region = region;
    }

    public Address() {

    }
}
