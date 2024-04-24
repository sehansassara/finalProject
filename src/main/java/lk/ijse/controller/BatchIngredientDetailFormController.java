package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lk.ijse.model.BatchEmployee;
import lk.ijse.model.tm.BatchEmployeeTm;
import lk.ijse.repository.BatchEmployeeRepo;

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
    private TableView<?> tblBatchIng;

    @FXML
    private TextField txtBatId;

    @FXML
    private TextField txtIngId;

    @FXML
    private TextField txtQty;

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

}
