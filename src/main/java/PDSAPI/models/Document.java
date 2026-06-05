package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Document {
    private String dateOfIssue;
    private String kodPodrazd;
    private String number;
    private String placeOfIssue;
    private String series;
    private String type;

    public Document() {

    }

    public Document(String dateOfIssue, String kodPodrazd, String number, String placeOfIssue, String series, String type) {
        this.dateOfIssue = dateOfIssue;
        this.kodPodrazd = kodPodrazd;
        this.number = number;
        this.placeOfIssue = placeOfIssue;
        this.series = series;
        this.type = type;
    }
}
