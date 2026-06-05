package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PaymentsPlan {

    private List<Payment> payments;
}
