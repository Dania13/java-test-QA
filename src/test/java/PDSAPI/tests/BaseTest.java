package PDSAPI.tests;

import PDSAPI.actions.Auth;
import PDSAPI.specs.ConstantValues;
import org.junit.jupiter.api.BeforeAll;


public class BaseTest {

    @BeforeAll
    public static void globalSetup() {
        //Первичная авторизация для получения кеша
        Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }
}