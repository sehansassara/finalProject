package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchTm {
    private String batId;
    private String stoId;
    private String ordId;
    private String type;
    private String productionDate;
    private int numberOfReject;
    private int qty;
}
