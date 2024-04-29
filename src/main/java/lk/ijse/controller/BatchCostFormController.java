package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.*;
import lk.ijse.model.tm.BatchCostTm;
import lk.ijse.model.tm.OrderTm;
import lk.ijse.repository.BatchCostRepo;
import lk.ijse.repository.BatchRepo;
import lk.ijse.repository.IngredientRepo;
import lk.ijse.repository.PlaceOrderRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchCostFormController {
    @FXML
    private JFXButton btnAddToTable;

    @FXML
    private JFXButton btnPlaceCost;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private JFXComboBox<String> comBatId;

    @FXML
    private JFXComboBox<String> comIngId;

    @FXML
    private Label lblBatchType;

    @FXML
    private Label lblIngType;

    @FXML
    private Label lblProductionDate;

    @FXML
    private Label lblQtyOnHand;
    @FXML
    private Label lblBatchUnitPrice;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private Label lblTotalCost;

    @FXML
    private TableView<BatchCostTm> tblCost;

    @FXML
    private TextField txtQty;

    private ObservableList<BatchCostTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        getBatIds();
        getIngIds();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }

    private void getIngIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> ingList = IngredientRepo.getIds();

            for (String id : ingList) {
                obList.add(id);
            }
            comIngId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getBatIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> batIdList = BatchRepo.getIds();

            for (String id : batIdList) {
                obList.add(id);
            }
            comBatId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ComBatIdOnAction(ActionEvent event) {
        String batId = comBatId.getValue();

        try {
            Batch batch = BatchRepo.searchById(batId);
            if(batch != null) {
                lblBatchUnitPrice.setText(String.valueOf(batch.getPrice()));
                lblBatchType.setText(String.valueOf(batch.getType()));
                lblProductionDate.setText(String.valueOf(batch.getProductionDate()));
            }

            txtQty.requestFocus();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnAddToTableOnAction(ActionEvent event) {
        String batId = comBatId.getValue();
        String ingId = comIngId.getValue();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = qty * unitPrice;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> types = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(types.orElse(no) == yes) {
                int selectedIndex = tblCost.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblCost.refresh();
                calculateNetTotal();
            }
        });

        for (int i = 0;i<tblCost.getItems().size(); i++){
            if (ingId.equals(colIngId.getCellData(i))) {

                BatchCostTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblCost.refresh();

                calculateNetTotal();
                return;
            }
        }
        BatchCostTm tm = new BatchCostTm(batId, ingId, unitPrice, qty, total, btnRemove);
        obList.add(tm);
        System.out.println("fhfjccf");
        tblCost.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");
        }

    private void calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < tblCost.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblTotalCost.setText(String.valueOf(netTotal));
    }

    @FXML
    void btnPlaceCostOnAction(ActionEvent event) {
        List<batchIngredient> bcList = new ArrayList<>();

        for (int i = 0; i < tblCost.getItems().size(); i++) {
            BatchCostTm tm = obList.get(i);

            batchIngredient od = new batchIngredient(
                    tm.getBatId(),
                    tm.getQty(),
                    tm.getIngId()
            );

            bcList.add(od);
        }

        BatchCost bc = new BatchCost(bcList);

        try {
            boolean isPlaced = BatchCostRepo.placeCost(bc);
            if (isPlaced){
                new Alert(Alert.AlertType.CONFIRMATION, "batch cost Placed!").show();
                obList.clear();
                tblCost.setItems(obList);
                calculateNetTotal();
            }else{
                new Alert(Alert.AlertType.WARNING, "batch cost Placed Unsuccessfully!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comIngIdOnAction(ActionEvent event) {
        String ingId = comIngId.getValue();

        try {
            Ingredient ingredient = IngredientRepo.searchById(ingId);
            if(ingredient != null) {
                lblIngType.setText(String.valueOf(ingredient.getType()));
                lblUnitPrice.setText(String.valueOf(ingredient.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(ingredient.getQtyOnHand()));
            }

            txtQty.requestFocus();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToTableOnAction(event);
    }

}
