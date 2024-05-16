package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.controller.Util.Regex;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Employee;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.EmployeeRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colPosition;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPosition;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtTel;
    public void initialize() {
        setCellValueFactory();
        loadAllEmployee();
        getCurrentEmpIds();
    }

    private void getCurrentEmpIds() {
        try {
            String currentId = EmployeeRepo.getCurrentId();

            String nextCusId = generateNexrEmpId(currentId);
            txtEmpId.setText(nextCusId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrEmpId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("E");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "E" + String.format("%03d", ++idNum);
        }
        return "E001";
    }


    private void setCellValueFactory() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
    }

    private void loadAllEmployee() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList){
                EmployeeTm tm = new EmployeeTm(
                        employee.getEmpId(),
                        employee.getFirstName(),
                        employee.getAddress(),
                        employee.getTel(),
                        employee.getSalary(),
                        employee.getPosition(),
                        new JFXButton()


                );
                obList.add(tm);
            }

            tblEmployee.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtEmpId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtAddress.setText("");
        txtTel.setText("");
        txtSalary.setText("");
        txtPosition.setText("");

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtEmpId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter employee ID.").show();
            return;
        }

        try {
            boolean isDeleted = EmployeeRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"employee is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllEmployee();
        clearFields();
        getCurrentEmpIds();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtEmpId.getText();
        String fName = txtFirstName.getText();
        String lName = txtLastName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        double salary = 0.0;
        if (!txtSalary.getText().isEmpty()) {
            salary = Double.parseDouble(txtSalary.getText());
        }

        String position = txtPosition.getText();

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Employee employee = new Employee(id,fName,lName,address,tel,salary,position);

        try {
            if (id.isEmpty() || fName.isEmpty() || lName.isEmpty() || address.isEmpty() || tel.isEmpty() || position.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").show();
                return;
            }

            boolean isSaved = EmployeeRepo.save(employee);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"employee is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllEmployee();
        clearFields();
        getCurrentEmpIds();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtEmpId.getText();
        String fName = txtFirstName.getText();
        String lName = txtLastName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        double salary = 0.0;
        if (!txtSalary.getText().isEmpty()) {
            salary = Double.parseDouble(txtSalary.getText());
        }

        String position = txtPosition.getText();


        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Employee employee = new Employee(id, fName, lName, address, tel, salary, position);

        try {
            if (id.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please enter employee ID.").show();
                return;
            }

            boolean isUpdated = EmployeeRepo.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllEmployee();
        clearFields();
        getCurrentEmpIds();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtEmpId.getText();

        try {
            Employee employee = EmployeeRepo.searchById(id);
            if (employee != null){
                txtEmpId.setText(employee.getEmpId());
                txtFirstName.setText(employee.getFirstName());
                txtLastName.setText(employee.getLastName());
                txtAddress.setText(employee.getAddress());
                txtTel.setText(employee.getTel());
                txtSalary.setText(String.valueOf(employee.getSalary()));
                txtPosition.setText(employee.getPosition());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"employee is not found !").show();
        }
    }

    public void tblEmpOnMouse(MouseEvent mouseEvent) {
        int index = tblEmployee.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String empId = colEmpId.getCellData(index).toString();
        String name = colFirstName.getCellData(index).toString();
        String address = colAddress.getCellData(index).toString();
        String tel = colTel.getCellData(index).toString();
        String salary = colSalary.getCellData(index).toString();
        String position = colPosition.getCellData(index).toString();

        txtEmpId.setText(empId);
        txtFirstName.setText(name);
        txtAddress.setText(address);
        txtTel.setText(tel);
        txtSalary.setText(salary);
        txtPosition.setText(position);
    }

    @FXML
    void btnReportOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/EmployeeBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

       Map<String,Object> data = new HashMap<>();
        data.put("empId",txtEmpId.getText());
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport,data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtEmpId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.NAME,txtFirstName)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.NAME,txtLastName)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtTel)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.SALARY,txtSalary)) return false;
        return true;
    }
    @FXML
    void txtAddressOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress);
    }

    @FXML
    void txtEmpIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtEmpId);
    }

    @FXML
    void txtFirstNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.NAME,txtFirstName);
    }

    @FXML
    void txtLastNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.NAME,txtLastName);
    }

    @FXML
    void txtSalaryOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.SALARY,txtSalary);
    }

    @FXML
    void txtTelOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtTel);
    }
}
