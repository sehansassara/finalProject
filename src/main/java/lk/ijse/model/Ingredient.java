package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ingredient {
    private String ingId;
    private String type;
    private double unitPrice;
    private String qtyOnHand;
}
