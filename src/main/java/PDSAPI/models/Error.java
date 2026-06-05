package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Error {
    private String message;
    private String type;
    private String detailMessage;
}
