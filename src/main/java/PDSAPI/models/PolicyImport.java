package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PolicyImport {

    private String currCode;
    private String insCompanyName;
    private String comment;
    private Insurant insurant;
    private InsuranceObjects insuranceObjects;
    private Parameters parameters;
    private Product product;
}
