package helpers;

public class MaskParametsForLog {
    /**
     * Маскирует токен для безопасного логирования.
     *
     * @param token оригинальный токен
     * @return замаскированный токен (показывает только первые 6 и последние 4 символа)
     */
    public static String maskToken(String token) {
        if (token == null || token.length() <= 10) {
            return "***";
        }
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
}
