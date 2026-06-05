package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PrintResponse {
    private String number;
    private String url;
    private Errors errors;

    public PrintResponse(){

    }
}
