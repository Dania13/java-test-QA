package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalcRequest {
    private String productType;
    private PolicyCalc policyCalc;

    public CalcRequest(String productType, PolicyCalc policyCalc) {
        this.productType = productType;
        this.policyCalc = policyCalc;
    }
}


