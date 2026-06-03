package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InsuranceObjects {
    private List<PDSAPI.models.Object> objects;

    public InsuranceObjects(List<PDSAPI.models.Object> objects) {
        this.objects = objects;
    }
}