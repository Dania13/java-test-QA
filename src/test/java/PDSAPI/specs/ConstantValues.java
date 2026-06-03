package PDSAPI.specs;

import io.github.cdimascio.dotenv.Dotenv;

public class ConstantValues {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static final String BASE_URL = dotenv.get("BASE_URL") != null ? dotenv.get("BASE_URL") : "https://test.soglasie-vita.ru/PO.Insurance/services/partner";
    public static final String AUTH_ENDPOINT = "/v1/auth/login";
    public static final String DICT_ENDPOINT = "/v1/getDictionaries";
    public static final String CALC_ENDPOINT = "/v1/calculate";
    public static final String LOGIN_AUTH = dotenv.get("LOGIN_AUTH") != null ? dotenv.get("LOGIN_AUTH") : "";
    public static final String PASSWORD_AUTH = dotenv.get("PASSWORD_AUTH") != null ? dotenv.get("PASSWORD_AUTH") : "";
}