package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CalcPolicyResult {
    private List<CalcResults> calcResults;
}
