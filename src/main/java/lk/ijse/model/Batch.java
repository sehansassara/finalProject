package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Batch {
    private String batId;
    private String stoId;
    private double price;
    private String type;
    private Date productionDate;
    private int numberOfReject;
    private int qty;
}
