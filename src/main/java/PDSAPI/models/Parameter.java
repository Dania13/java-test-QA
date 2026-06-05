package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Parameter {

    private String code;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String stringValue;
    private String type;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer intValue;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double decimalValue;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean boolValue;


    public Parameter (String code, String name, String stringValue, String type) {
        this.code = code;
        this.name = name;
        this.stringValue = stringValue;
        this.type = type;
    }

    public Parameter (String code, String name, int intValue, String type) {
        this.code = code;
        this.name = name;
        this.intValue = intValue;
        this.type = type;
    }

    public Parameter (String code, String name, boolean boolValue, String type) {
        this.code = code;
        this.name = name;
        this.boolValue = boolValue;
        this.type = type;
    }

    public Parameter (String code, String name, double decimalValue, String type) {
        this.code = code;
        this.name = name;
        this.decimalValue = decimalValue;
        this.type = type;
    }

    public Parameter () {
    }

}