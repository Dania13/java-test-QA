package helpers;

import java.util.Random;

public class InnGenerator {

    public static String getINNFL(){
        final int[] coefficients1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
        final int[] coefficients2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
        Random random = new Random();
        StringBuilder innBuilder = new StringBuilder();

        // Генерируем первые 10 цифр
        for (int i = 0; i < 10; i++) {
            innBuilder.append(random.nextInt(10));
        }

        String inn = innBuilder.toString();

        // Вычисляем 11-ю контрольную цифру
        int sum11 = 0;
        for (int i = 0; i < 10; i++) {
            sum11 += Character.getNumericValue(inn.charAt(i)) * coefficients1[i];
        }
        int n11 = sum11 % 11;
        if (n11 == 10) n11 = 0;

        // Вычисляем 12-ю контрольную цифру
        int sum12 = 0;
        for (int i = 0; i < 10; i++) {
            sum12 += Character.getNumericValue(inn.charAt(i)) * coefficients2[i];
        }
        sum12 += n11 * coefficients2[10];
        int n12 = sum12 % 11;
        if (n12 == 10) n12 = 0;

        return inn + n11 + n12;

    }
}



