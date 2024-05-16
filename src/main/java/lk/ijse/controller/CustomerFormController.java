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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.controller.Util.Regex;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;
import lk.ijse.model.tm.CustomerTm;
import lk.ijse.repository.CustomerRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getCurrentCusIds();
        setCellValueFactory();
        loadAllCustomer();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }
    String nextCusId = "";
    private void getCurrentCusIds() {
        try {
            String currentId = CustomerRepo.getCurrentId();

            nextCusId = generateNexrCusId(currentId);
            txtCusId.setText(nextCusId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrCusId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("C");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "C" + String.format("%03d", ++idNum);
        }
        return "C001";
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

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter customer ID.").show();
            return;
        }

        try {
            boolean isDeleted = CustomerRepo.delete(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCustomer();
        clearFields();
        getCurrentCusIds();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || tel.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }
            Customer customer = new Customer(id, name, address, tel);

            try {
                boolean isSaved = CustomerRepo.save(customer);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer is saved").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
                loadAllCustomer();
                clearFields();
                getCurrentCusIds();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = txtCusId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter customer ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Customer customer = new Customer(id, name, address, tel);

        try {
            boolean isUpdate = CustomerRepo.update(customer);
            if (isUpdate) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer is updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCustomer();
        clearFields();
        getCurrentCusIds();
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

    public void tblCustOnMouse(MouseEvent mouseEvent) {
        int index = tblCustomer.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String id = colId.getCellData(index).toString();
        String name = colName.getCellData(index).toString();
        String address = colAddress.getCellData(index).toString();
        String tel = colTel.getCellData(index).toString();


        txtCusId.setText(id);
        txtName.setText(name);
        txtAddress.setText(address);
        txtTel.setText(tel);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtCusId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtTel)) return false;

        return true;
    }
    @FXML
    void txtAddressOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress);

    }

    @FXML
    void txtTelOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtTel);

    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.NAME,txtName);
    }


    @FXML
    void txtCusIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtCusId);
    }

    @FXML
    void btnBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Customer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("cusId",txtCusId.getText());
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport,data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}
