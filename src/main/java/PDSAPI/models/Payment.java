package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {

    private int number;
    private String date;
    private double sum;
    private double sumRur;
}
