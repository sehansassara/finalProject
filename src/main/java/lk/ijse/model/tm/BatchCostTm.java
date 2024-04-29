package lk.ijse.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchCostTm {
    private String batId;
    private String ingId;
    private double unitPrice;
    private int qty;
    private double total;
    private JFXButton btnRemove;
}
