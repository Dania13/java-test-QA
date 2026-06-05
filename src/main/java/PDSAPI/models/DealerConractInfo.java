package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DealerConractInfo {
    private String type;
    private AgentConract agentConract;
    private Dealer dealer;
}
