package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SupplierTm {
    private String supId;
    private String companyName;
    private String address;
    private String contact;
    private String ingId;
}
