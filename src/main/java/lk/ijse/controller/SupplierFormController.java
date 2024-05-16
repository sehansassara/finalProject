package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.model.Ingredient;
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.IngredientRepo;
import lk.ijse.repository.SupplierRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCompanyName;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCompanyName;

    @FXML
    private TextField txtContact;

    @FXML
    private JFXComboBox<String> comIngId;

    @FXML
    private TextField txtIngId;

    @FXML
    private TextField txtSupId;
    public void initialize() {
        setCellValueFactory();
        loadAllSupplier();
        getIngrediantIds();
        getCurrentSupIds();
    }

    private void getCurrentSupIds() {
        try {
            String currentId = SupplierRepo.getCurrentId();

            String nextCusId = generateNexrSupId(currentId);
            txtSupId.setText(nextCusId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrSupId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("S");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "S" + String.format("%03d", ++idNum);
        }
        return "S001";
    }

    private void getIngrediantIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();


        try {
            List<String> idList = IngredientRepo.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comIngId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
    }

    private void loadAllSupplier() {
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();
        try {
            List<Supplier> supplierList = SupplierRepo.getAll();
            for (Supplier supplier : supplierList){
                SupplierTm tm = new SupplierTm(
                        supplier.getSupId(),
                        supplier.getCompanyName(),
                        supplier.getAddress(),
                        supplier.getContact(),
                        supplier.getIngId()
                );
                obList.add(tm);
            }
            tblSupplier.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtSupId.setText("");
        txtCompanyName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        comIngId.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter supplier ID.").show();
            return;
        }

        try {
            boolean isDeleted = SupplierRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"supplier is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllSupplier();
        clearFields();
        getCurrentSupIds();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String ingId = comIngId.getValue();

        if (id.isEmpty() || companyName.isEmpty() || address.isEmpty() || contact.isEmpty() || ingId == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Supplier supplier = new Supplier(id, companyName, address, contact, ingId);

        try {
            boolean isSaved = SupplierRepo.save(supplier);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllSupplier();
        clearFields();
        getCurrentSupIds();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String ingId = comIngId.getValue();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter supplier ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Supplier supplier = new Supplier(id, companyName, address, contact, ingId);

        try {
            boolean isUpdated = SupplierRepo.updated(supplier);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllSupplier();
        clearFields();
        getCurrentSupIds();
    }

    @FXML
    void ComIngIdOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        try {
            Ingredient ingredient = IngredientRepo.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        try {
            Supplier supplier = SupplierRepo.searchById(id);
            if (supplier != null){
                txtSupId.setText(supplier.getSupId());
                txtCompanyName.setText(supplier.getCompanyName());
                txtAddress.setText(supplier.getAddress());
                txtContact.setText(supplier.getContact());
                comIngId.setValue(supplier.getIngId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"supplier is not found !").show();
        }
    }

    public void tblSupOnMouse(MouseEvent mouseEvent) {
        int index = tblSupplier.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String supId = colSupId.getCellData(index).toString();
        String companyName = colCompanyName.getCellData(index).toString();
        String address = colAddress.getCellData(index).toString();
        String contact = colContact.getCellData(index).toString();
        String ingId = colIngId.getCellData(index).toString();

        txtSupId.setText(supId);
        txtCompanyName.setText(companyName);
        txtAddress.setText(address);
        txtContact.setText(contact);
        comIngId.setValue(ingId);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtSupId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtContact)) return false;
        return true;
    }
    @FXML
    void txtAddressOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtAddress);
    }

    @FXML
    void txtContactOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.CONTACT,txtContact);
    }

    @FXML
    void txtSupIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtSupId);
    }

    @FXML
    void txtCompanyKeyReleasedOAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtCompanyName);
    }


    @FXML
    void btnBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Supplier.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("supId",txtSupId.getText());
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport,data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}
