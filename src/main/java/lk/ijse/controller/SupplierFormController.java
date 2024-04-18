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
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.SupplierRepo;

import java.sql.SQLException;
import java.util.List;

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
    private TextField txtIngId;

    @FXML
    private TextField txtSupId;
    public void initialize() {
        setCellValueFactory();
        loadAllSupplier();
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
        txtIngId.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        try {
            boolean isDeleted = SupplierRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"supplier is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String ingId = txtIngId.getText();

        Supplier supplier = new Supplier(id,companyName,address,contact,ingId);

        try {
            boolean isSaved = SupplierRepo.save(supplier);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"supplier is saved").show();
            }
        } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        String ingId = txtIngId.getText();

        Supplier supplier = new Supplier(id,companyName,address,contact,ingId);

        try {
            boolean isUpdated = SupplierRepo.updated(supplier);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"supplier is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtSupId.getText();

        try {
            Supplier supplier = SupplierRepo.searchById(id);
            if (supplier != null){
                txtIngId.setText(supplier.getIngId());
                txtCompanyName.setText(supplier.getCompanyName());
                txtAddress.setText(supplier.getAddress());
                txtContact.setText(supplier.getContact());
                txtIngId.setText(supplier.getIngId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"supplier is not found !").show();
        }
    }

}
