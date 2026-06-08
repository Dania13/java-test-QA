package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Контейнер для списка предупреждений при обработке полиса.
 * <p>Используется для возврата некритичных замечаний, которые не блокируют
 * выполнение операции, но требуют внимания пользователя.</p>
 */
@Setter
@Getter
public class Warnings {
    /** Список предупреждений (использует структуру {@link Error}) */
    private List<Error> errors;

    /**
     * Проверяет, есть ли предупреждения.
     *
     * @return true, если список предупреждений не пуст
     */
    public boolean hasWarnings() {
        return errors != null && !errors.isEmpty();
    }
}