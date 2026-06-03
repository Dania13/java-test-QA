package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DictRequest {
    private String accID;
    private String product;
    private String dictionaryCode;
}
