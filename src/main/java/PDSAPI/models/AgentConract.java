package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AgentConract {

    private Agent agent;
    private String number;
    private String date;
    private String departmentCode;
    private String ikp;
    private String salesChannel;
}
