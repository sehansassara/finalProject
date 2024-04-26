package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Batch {
    private String batId;
    private String stoId;
    private double price;
    private String type;
    private String productionDate;
    private int numberOfReject;
    private int qty;
}
