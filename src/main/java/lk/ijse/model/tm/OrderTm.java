package lk.ijse.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderTm {
    private String batId;
    private String cusId;
    private String type;
    private double unitPrice;
    private int qty;
    private double total;
    private JFXButton btnRemove;
}
