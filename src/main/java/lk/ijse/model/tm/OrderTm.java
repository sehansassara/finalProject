package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderTm {
    private String ordId;
    private String cusId;
    private String dateOfPlace;
    private String payId;
    private String dateOfRelease;
}
