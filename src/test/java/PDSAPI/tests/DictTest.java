package PDSAPI.tests;

import PDSAPI.models.AuthRequest;
import PDSAPI.models.AuthResponse;
import PDSAPI.models.DictRequest;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DictTest {

    private AuthResponse responseAuth;

    @BeforeEach
    public void auth(){
        AuthRequest user = AuthRequest.builder()
                .login(ConstantValues.LOGIN_AUTH)
                .password(ConstantValues.PASSWORD_AUTH).build();
        responseAuth = given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(user).
                when()
                .post(ConstantValues.AUTH_ENDPOINT).
                then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class);
    }


    @Test
    public void successGetDict(){
        DictRequest dict = DictRequest.builder()
                .accID(responseAuth.getSessionToken())
                .product("ПДС")
                .dictionaryCode("region")
                .build();
        given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(dict).
        when()
                .post(ConstantValues.DICT_ENDPOINT).
        then()
                .statusCode(200)
                .body("types.dictionaries.code", equalTo("region"))
                .log().all();
    }
}
