package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PrintRequest {
    private String calcID;
    private String type;
}
