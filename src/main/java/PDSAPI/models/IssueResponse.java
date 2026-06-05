package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class IssueResponse {
    private String accID;
    private String policyID;
    private Errors errors;

    public IssueResponse(){

    }
}
