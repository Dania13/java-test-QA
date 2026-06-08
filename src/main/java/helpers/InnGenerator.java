package helpers;

import java.util.Random;

/**
 * Утилитарный класс для генерации валидных ИНН физических лиц (12 цифр).
 * <p>
 * Генерирует ИНН с корректно вычисленными 11-й и 12-й контрольными цифрами
 * в соответствии с алгоритмом расчёта для физических лиц РФ.
 * </p>
 *
 * @see <a href="https://ru.wikipedia.org/wiki/Идентификационный_номер_налогоплательщика">Алгоритм расчёта ИНН</a>
 */
public class InnGenerator {

    /**
     * Первый набор весовых коэффициентов для расчёта 11-й контрольной цифры ИНН.
     * Используется для позиций 1-10.
     */
    private static final int[] COEFFICIENTS_1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    /**
     * Второй набор весовых коэффициентов для расчёта 12-й контрольной цифры ИНН.
     * Используется для позиций 1-10, затем добавляется 11-я контрольная цифра.
     */
    private static final int[] COEFFICIENTS_2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    /**
     * Генерирует валидный 12-значный ИНН для физического лица.
     *
     * @return строка, содержащая 12 цифр, образующих корректный ИНН
     */
    public static String getINNFL(){

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
            sum11 += Character.getNumericValue(inn.charAt(i)) * COEFFICIENTS_1[i];
        }
        int n11 = sum11 % 11;
        if (n11 == 10) n11 = 0;

        // Вычисляем 12-ю контрольную цифру
        int sum12 = 0;
        for (int i = 0; i < 10; i++) {
            sum12 += Character.getNumericValue(inn.charAt(i)) * COEFFICIENTS_2[i];
        }
        sum12 += n11 * COEFFICIENTS_2[10];
        int n12 = sum12 % 11;
        if (n12 == 10) n12 = 0;

        return inn + n11 + n12;

    }
}



