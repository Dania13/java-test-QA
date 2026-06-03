package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Object {

    private String name;
    private java.lang.Object parameters;
    private RiskInfo riskInfo;

    public Object(String name, java.lang.Object parameters, RiskInfo riskInfo) {
        this.name = name;
        this.parameters = parameters;
        this.riskInfo = riskInfo;
    }
}