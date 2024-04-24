package lk.ijse.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeTm {
    private String empId;
    private String  firstName;
    private String address;
    private String tel;
    private double salary;
    private String position;
    private JFXButton btnSave;
  //  private JFXButton btnRemove;
}
