package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {

    private String name;

    public Product(String name) {
        this.name = name;
    }

    public Product() {

    }
}
