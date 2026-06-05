package PDSAPI.tests;

import PDSAPI.actions.Auth;
import PDSAPI.models.DictRequest;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class DictTests {

    private String sessionToken;

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.BASE_URL,
                ConstantValues.AUTH_ENDPOINT,
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }


    @Test
    @Disabled("в процессе отладки")
    public void successGetDict(){
        DictRequest dict = DictRequest.builder()
                .accID(sessionToken)
                .product("ПДС")
                .dictionaryCode("region")
                .build();
        given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(dict)
                .log().all().
        when()
                .post(ConstantValues.DICT_ENDPOINT).
        then()
                .statusCode(200)
//                .body("types.dictionaries.code", equalTo("region"))
                .log().all();
    }
}
