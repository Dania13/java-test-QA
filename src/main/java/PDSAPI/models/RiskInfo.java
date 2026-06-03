package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RiskInfo {
    private List<Risk> risks;

    public RiskInfo(List<Risk> risks) {
        this.risks = risks;
    }
}
