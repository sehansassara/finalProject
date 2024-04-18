package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.Batch;
import lk.ijse.model.tm.BatchTm;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.BatchRepo;

import java.sql.SQLException;
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
    private TextField txtBatId;

    @FXML
    private TextField txtNumOfReject;

    @FXML
    private TextField txtOrdId;

    @FXML
    private TextField txtProductionDate;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtStoId;
    public void initialize() {
        setCellValueFactory();
        loadAllIngredient();

        ObservableList<String> batchType = FXCollections.observableArrayList("Jack Mackerel", "Tuna Mackeral","Mackerel","Sardin");
        choiceType.setItems(batchType);
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

    private void loadAllIngredient() {
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
        txtStoId.setText("");
        txtOrdId.setText("");
        txtProductionDate.setText("");
        txtNumOfReject.setText("");
        txtQty.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        try {
            boolean isDeleted = BatchRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = txtStoId.getText();
        String ordId = txtOrdId.getText();
        String type = (String) choiceType.getValue();
        String productionDate = txtProductionDate.getText();
        int numOfReject = Integer.parseInt(txtNumOfReject.getText());
        int qty = Integer.parseInt(txtQty.getText());

        Batch batch = new Batch(id,stoId,ordId,type,productionDate,numOfReject,qty);

        try {
            boolean isSaved = BatchRepo.save(batch);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = txtStoId.getText();
        String ordId = txtOrdId.getText();
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

        Batch batch = new Batch(id,stoId,ordId,type,productionDate,numOfReject,qty);

        try {
            boolean isUpdated = BatchRepo.update(batch);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"batch is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        try {
            Batch batch = BatchRepo.searchById(id);
            if(batch != null){
                txtBatId.setText(batch.getBatId());
                txtStoId.setText(batch.getStoId());
                txtOrdId.setText(batch.getOrdId());
                txtProductionDate.setText(batch.getProductionDate());
                txtNumOfReject.setText(String.valueOf(batch.getNumberOfReject()));
                txtQty.setText(String.valueOf(batch.getQty()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"batch is not found !").show();
        }
    }

}
