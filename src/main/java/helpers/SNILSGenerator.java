package helpers;

import java.util.Random;

public class SNILSGenerator {

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



