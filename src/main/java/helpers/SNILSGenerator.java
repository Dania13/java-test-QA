package helpers;

import java.util.Random;

/**
 * Утилитарный класс для генерации валидных номеров СНИЛС.
 * <p>
 * Генерирует 11-значный номер СНИЛС (9 цифр номера + 2 контрольные цифры)
 * с корректным расчётом контрольной суммы в соответствии с законодательством РФ.
 * </p>
 *
 * @see <a href="https://sfr.gov.ru/grazhdanam/personificirovannyj_uchet/snils/">Официальная информация о СНИЛС</a>
 */
public class SNILSGenerator {

    /**
     * Генерирует валидный номер СНИЛС.
     * <p>
     * Алгоритм: сумма произведений первых 9 цифр на веса от 9 до 1,
     * затем вычисление остатка от деления на 101. Если остаток равен 100,
     * контрольное число становится 00.
     * </p>
     *
     * @param formatted если true, возвращает номер в форматированном виде "XXX-XXX-XXX XX",
     *                  если false — в неформатированном виде как 11 цифр подряд
     * @return строка с валидным номером СНИЛС в указанном формате
     */
    public static String getSNILS(boolean formatted){
        Random random = new Random();
        StringBuilder baseBuilder = new StringBuilder();

        // Генерируем первые 9 цифр
        for (int i = 0; i < 9; i++) {
            baseBuilder.append(random.nextInt(10));
        }

        String base = baseBuilder.toString();

        // Вычисляем контрольную сумму
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(base.charAt(i)) * (9 - i);
        }

        int checkSum = sum % 101;
        if (checkSum == 100) checkSum = 0;

        // Формируем полный номер СНИЛС
        String fullNumber = base + String.format("%02d", checkSum);

        if (formatted) {
            // Форматируем в виде XXX-XXX-XXX XX
            return fullNumber.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1-$2-$3 $4");
        }

        return fullNumber;
    }
}



