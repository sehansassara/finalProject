package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Customer;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.model.tm.CustomerTm;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomerFormController {

    public JFXButton btnBack;
    public JFXButton btnClear;
    @FXML
    private TableView<CustomerTm> tblCustomer;
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;


    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private AnchorPane custAnchorpane;

    @FXML
    private TextField txtCusId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtTel;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomer();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }

    private void loadAllCustomer() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for(Customer customer : customerList){
                CustomerTm tm = new CustomerTm(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getTel()
                );
                obList.add(tm);
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtCusId.getText();

        try {
            boolean isDeleted = CustomerRepo.delete(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        Customer customer = new Customer(id,name,address,tel);

        try {
            boolean isSaved = CustomerRepo.save(customer);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Customer is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCustomer();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = txtCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        Customer customer = new Customer(id, name, address, tel);

        try {
            boolean isUpdate = CustomerRepo.update(customer);
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"customer is updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    @FXML
    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }
    private void clearFields() {
        txtCusId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtTel.setText("");
    }
    @FXML
    void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashBoard_form.fxml"));
        Stage stage = (Stage) custAnchorpane.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtCusId.getText();

        Customer customer = CustomerRepo.searchById(id);

        if (customer != null) {
            txtCusId.setText(customer.getId());
            txtName.setText(customer.getName());
            txtTel.setText(customer.getTel());
            txtAddress.setText(customer.getAddress());
        }else {
            new Alert(Alert.AlertType.INFORMATION,"customer is not found !").show();
        }
    }
}
