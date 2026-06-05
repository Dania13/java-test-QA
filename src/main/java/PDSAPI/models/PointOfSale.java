package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PointOfSale {
    private String name;
    private String code;
    private Address address;
}
