package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AnnulResponse {
    private String accID;
    private String calcID;
    private boolean ok;
    private Errors errors;

    public AnnulResponse(){

    }
}
