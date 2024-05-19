package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.batchIngredient;
import lk.ijse.model.tm.batchIngredientTM;
import lk.ijse.repository.BatchIngredientRepo;

import java.sql.SQLException;
import java.util.List;

public class BatchIngredientDetailFormController {

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableView<batchIngredientTM> tblBatchIng;

    @FXML
    private TextField txtBatId;

    @FXML
    private TextField txtIngId;

    @FXML
    private TextField txtQty;

    public void initialize() {
        setCellValueFactory();
        loadAllBatchIng();
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
    }

    private void loadAllBatchIng() {
        ObservableList<batchIngredientTM> batIng = FXCollections.observableArrayList();
        try {
            List<batchIngredient> batIng1 = BatchIngredientRepo.getAll();
            for (batchIngredient batchIngredient : batIng1){
                batchIngredientTM tmt = new batchIngredientTM(
                        batchIngredient.getBatId(),
                        batchIngredient.getIngId(),
                        batchIngredient.getQty()
                );
                batIng.add(tmt);
            }
            tblBatchIng.setItems(batIng);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }
    }

}
