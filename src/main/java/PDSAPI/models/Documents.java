package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Обёртка для списка документов.
 * <p>Используется для передачи коллекции документов в запросах и ответах API.</p>
 */
@Setter
@Getter
public class Documents {

    /** Список документов */
    private List<Document> documents;
}
