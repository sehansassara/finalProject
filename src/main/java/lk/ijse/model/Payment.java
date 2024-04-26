package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {
    private String payId;
    private double amount;
    private Date date;
    private String type;
    private String ordId;
}
