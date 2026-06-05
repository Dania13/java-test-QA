package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttachRequest {
    private String calcID;
    private String fileName;
    private String type;
    private String attachment;

    public AttachRequest(String calcID, String type) {
        this.calcID = calcID;
        this.fileName = "test.txt";
        this.type = type;
        this.attachment = "0KLQtdGB0YLQvtCy0YvQuSDQtNC+0LrRg9C80LXQvdGC";
    }
}
