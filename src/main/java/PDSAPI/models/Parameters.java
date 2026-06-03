package PDSAPI.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Parameters {

    private List<Parameter> parameters;

    public Parameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
