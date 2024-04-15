package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IngredientTm {
    private String ingId;
    private String type;
    private double unitPrice;
    private String qtyOnHand;
}
