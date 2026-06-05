package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalcResponse {

    private String accID;
    private CalcPolicyResult calcPolicyResult;
}
