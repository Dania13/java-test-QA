package PDSAPI.specs;

public class ConstantValues {
    public static final String BASE_URL = System.getenv("BASE_URL") != null ? System.getenv("BASE_URL") : "";
    public static final String AUTH_ENDPOINT = "/v1/auth/login";
    public static final String DICT_ENDPOINT = "/v1/getDictionaries";
    public static final String CALC_ENDPOINT = "/v1/calculate";
    public static final String LOGIN_AUTH  = System.getenv("LOGIN_AUTH") != null ? System.getenv("LOGIN_AUTH") : "";
    public static final String PASSWORD_AUTH  = System.getenv("PASSWORD_AUTH") != null ? System.getenv("PASSWORD_AUTH") : "";
}
