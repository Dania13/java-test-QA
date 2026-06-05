package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Policy {
    private String insCompanyName;
    private Product product;
    private String number;
    private String date;
    private String state;
    private Boolean prolongation;
    private Insurant insurant;
    private String insurantDocType;
    private InsuranceObjects insuranceObjects;
    private Double insPremTotal;
    private String comment;
    private Representative representative;
    private CurrExchList currExchList;
    @JsonProperty("ID")
    private String ID;
    private DealerConractInfo dealerConractInfo;
    private PointOfSale pointOfSale;
    private UserInfo userInfo;
    private String currCode;
    private String calcID;
    private PaymentsPlan paymentsPlan;
    private Double discountAV;
    private Double discountUnderwriter;
    private Double ksp;
    private Boolean clientWithoutLoss;
    private Boolean foulOfInsurance;
    private String dateCalc;
    private Integer internalID;
    private Boolean pechatNaBlanke;
    private Boolean skidkaAvVProc;
    private Parameters parameters;
    private Boolean epolicy;
    private Boolean onlinePayment;

    public Policy() {

    }
}
