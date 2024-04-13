package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private String ordId;
    private String cusId;
    private String dateOfPlace;
    private String payId;
    private String dateOfRelease;

}
