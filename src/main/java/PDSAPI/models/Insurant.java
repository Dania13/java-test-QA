package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Insurant {
    private Physical physical;
    private String type;

    public Insurant(Physical physical, String type) {
        this.physical = physical;
        this.type = type;
    }

    public Insurant() {

    }
}
