package PDSAPI.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ImportRequest {
    private PolicyImport policy;

    public ImportRequest(PolicyImport policy) {
        this.policy = policy;
    }
}


