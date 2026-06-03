package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthRequest {
    private String login;
    private String password;

//    public AuthRequest() {}
//
//    public AuthRequest(String login, String password) {
//        this.login = login;
//        this.password = password;
//    }

}
