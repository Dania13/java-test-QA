package PDSAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private String name;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }
}
