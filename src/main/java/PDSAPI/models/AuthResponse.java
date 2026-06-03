package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {

    private String sessionToken;
    private boolean success;
    private String message;

    public AuthResponse() {}

    public boolean getSuccess() { return success; }

}
