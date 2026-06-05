package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Documents {

    private List<Document> documents;

    public Documents() {
    }
}
