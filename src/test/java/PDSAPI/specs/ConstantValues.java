package PDSAPI.specs;

import io.github.cdimascio.dotenv.Dotenv;

public class ConstantValues {
    private static String getEnvValue(String key) {
        // 1. Сначала пробуем получить из системных переменных (GitHub Secrets)
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }

        // 2. Пробуем загрузить из .env файла (локально)
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            value = dotenv.get(key);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        } catch (Exception e) {
            // Игнорируем - просто нет .env файла
        }

        // 3. Возвращаем значение по умолчанию
        return "";
    }

//    Эндпоинты
    public static final String AUTH_ENDPOINT = "/v1/auth/login";
    public static final String DICT_ENDPOINT = "/v1/getDictionaries";
    public static final String CALC_ENDPOINT = "/v1/calculate";
    public static final String IMPORT_ENDPOINT = "/v1/import";
    public static final String ATTACH_ENDPOINT = "/v1/attachDoc";
    public static final String ISSUE_ENDPOINT = "/v1/issue";

//    Переменные окружения
    public static final String BASE_URL = getEnvValue("BASE_URL");
    public static final String LOGIN_AUTH = getEnvValue("LOGIN_AUTH");
    public static final String PASSWORD_AUTH = getEnvValue("PASSWORD_AUTH");
}