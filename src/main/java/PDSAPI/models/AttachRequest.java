package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AttachRequest {
    private String calcID;
    private String fileName;
    private String type;
    private String comment;
    private String attachment;



}
