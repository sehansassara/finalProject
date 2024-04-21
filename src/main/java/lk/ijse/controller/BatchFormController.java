package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.Batch;
import lk.ijse.model.Customer;
import lk.ijse.model.Order;
import lk.ijse.model.Store;
import lk.ijse.model.tm.BatchTm;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.BatchRepo;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;
import lk.ijse.repository.StoreRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchFormController {

    @FXML
    private TableView<BatchTm> tblBatch;

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colNumberOfRejectedItem;

    @FXML
    private TableColumn<?, ?> colOrdId;

    @FXML
    private TableColumn<?, ?> colProductionDate;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colStoId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private JFXComboBox<String> comOrdId;

    @FXML
    private JFXComboBox<String> comStoId;
    @FXML
    private TextField txtBatId;

    @FXML
    private TextField txtNumOfReject;

    @FXML
    private TextField txtProductionDate;

    @FXML
    private TextField txtQty;

    public void initialize() {
        setCellValueFactory();
        loadAllBatch();
        getOrderIds();
        getStoreIds();

        ObservableList<String> batchType = FXCollections.observableArrayList("Jack Mackerel", "Tuna Mackeral","Mackerel","Sardin");
        choiceType.setItems(batchType);
    }

    private void getStoreIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> storeList = StoreRepo.getIds();
            for(String id : storeList){
                obList.add(id);
            }
            comStoId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getOrderIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> orderList = OrderRepo.getIds();
            for (String id : orderList){
                obList.add(id);
            }
            comOrdId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colStoId.setCellValueFactory(new PropertyValueFactory<>("stoId"));
        colOrdId.setCellValueFactory(new PropertyValueFactory<>("ordId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colProductionDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));
        colNumberOfRejectedItem.setCellValueFactory(new PropertyValueFactory<>("numberOfReject"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void loadAllBatch() {
        ObservableList<BatchTm> obList = FXCollections.observableArrayList();

        try {
            List<Batch> batchList = BatchRepo.getAll();
            for (Batch batch : batchList){
                BatchTm tm = new BatchTm(
                        batch.getBatId(),
                        batch.getStoId(),
                        batch.getOrdId(),
                        batch.getType(),
                        batch.getProductionDate(),
                        batch.getNumberOfReject(),
                        batch.getQty()
                );
                obList.add(tm);
            }
            tblBatch.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtBatId.setText("");
        comStoId.setValue(null);
        comOrdId.setValue(null);
        txtProductionDate.setText("");
        txtNumOfReject.setText("");
        txtQty.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter batch ID.").show();
            return;
        }

        try {
            boolean isDeleted = BatchRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllBatch();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = comStoId.getValue();
        String ordId = comOrdId.getValue();
        String type = (String) choiceType.getValue();
        String productionDate = txtProductionDate.getText();

        int numOfReject = 0;
        if (!txtNumOfReject.getText().isEmpty()) {
            numOfReject = Integer.parseInt(txtNumOfReject.getText());
        }

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        if (id.isEmpty() || stoId.isEmpty() || ordId.isEmpty() || type == null || productionDate.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Batch batch = new Batch(id,stoId,ordId,type,productionDate,numOfReject,qty);

        try {
            boolean isSaved = BatchRepo.save(batch);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllBatch();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = comStoId.getValue();
        String ordId = comOrdId.getValue();
        String type = (String) choiceType.getValue();
        String productionDate = txtProductionDate.getText();

        int numOfReject = 0;
        if (!txtNumOfReject.getText().isEmpty()) {
            numOfReject = Integer.parseInt(txtNumOfReject.getText());
        }

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter batch ID.").show();
            return;
        }

        Batch batch = new Batch(id,stoId,ordId,type,productionDate,numOfReject,qty);

        try {
            boolean isUpdated = BatchRepo.update(batch);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllBatch();
    }

    @FXML
    void comOrdIdOnAction(ActionEvent event) {
        String id = comOrdId.getValue();

        try {
            Order order = OrderRepo.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comStoIdOnAction(ActionEvent event) {
        String id = comStoId.getValue();

        try {
            Store store = StoreRepo.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        try {
            Batch batch = BatchRepo.searchById(id);
            if(batch != null){
                txtBatId.setText(batch.getBatId());
                comStoId.setValue(batch.getStoId());
                comOrdId.setValue(batch.getOrdId());
                txtProductionDate.setText(batch.getProductionDate());
                txtNumOfReject.setText(String.valueOf(batch.getNumberOfReject()));
                txtQty.setText(String.valueOf(batch.getQty()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"batch is not found !").show();
        }
    }

}
