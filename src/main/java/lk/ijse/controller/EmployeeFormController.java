package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.Employee;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.EmployeeRepo;

import java.sql.SQLException;
import java.util.List;

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
                        employee.getPosition()
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



        Employee employee = new Employee(id,fName,lName,address,tel,salary,position);

        try {
            if (id.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please enter employee ID.").show();
                return;
            }

            boolean isUpdated = EmployeeRepo.update(employee);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"employee is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllEmployee();
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

}
