package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportResponse {

    private Policy policy;
    private Errors errors;
    private Warnings warnings;

}


