package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Risk {

    private String insured;
    private String name;
    private Integer insPrem;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Double fransiza;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Koefficients koefficients;

    public Risk(String insured, String name, int insPrem) {
        this.insured = insured;
        this.name = name;
        this.insPrem = insPrem;
    }

    public Risk() {
    }

}
