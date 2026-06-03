package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Risk {

    private String insured;
    private String name;
    private Integer insPrem;

    public Risk(String insured, String name, int insPrem) {
        this.insured = insured;
        this.name = name;
        this.insPrem = insPrem;
    }

}
