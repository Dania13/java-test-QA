package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Dealer {
    private String type;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Juridical juridical;
}
