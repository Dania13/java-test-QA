package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PolicyCalc {

    private String currCode;
    private String insCompanyName;
    private InsuranceObjects insuranceObjects;
    private boolean onlinePayment;
    private Parameters parameters;
    private Product product;
}
