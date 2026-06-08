package PDSAPI.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * График страховых платежей (взносов).
 * <p>Содержит расписание всех платежей по страховому полису.</p>
 */
@Setter
@Getter
public class PaymentsPlan {
    /** Список платежей */
    private List<Payment> payments;
}
