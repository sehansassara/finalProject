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
import lk.ijse.model.Store;
import lk.ijse.model.tm.StoreTm;
import lk.ijse.repository.StoreRepo;

import java.sql.SQLException;
import java.util.List;

public class StoreFormController {

    @FXML
    private TableColumn<?, ?> colCapacity;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colStoId;

    @FXML
    private TableView<StoreTm> tblStore;

    @FXML
    private TextField txtCapacity;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtStoId;
    public void initialize() {
        setCellValueFactory();
        loadAllStore();
    }

    private void setCellValueFactory() {
        colStoId.setCellValueFactory(new PropertyValueFactory<>("stoId"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    private void loadAllStore() {
        ObservableList<StoreTm> obList = FXCollections.observableArrayList();

        try {
            List<Store> storeList = StoreRepo.getAll();
            for (Store store : storeList){
                StoreTm tm = new StoreTm(
                        store.getStoId(),
                        store.getCapacity(),
                        store.getLocation()
                );
                obList.add(tm);
            }
            tblStore.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtStoId.setText("");
        txtCapacity.setText("");
        txtLocation.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtStoId.getText();

        try {
            boolean isDeleted = StoreRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"store is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllStore();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtStoId.getText();
        int capacity = Integer.parseInt(txtCapacity.getText());
        String location = txtLocation.getText();

        Store store = new Store(id,capacity,location);

        try {
            boolean isSaved = StoreRepo.save(store);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"store is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllStore();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtStoId.getText();
        int capacity = Integer.parseInt(txtCapacity.getText());
        String location = txtLocation.getText();

        Store store = new Store(id,capacity,location);

        try {
            boolean isUpdate = StoreRepo.update(store);
            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"store is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllStore();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtStoId.getText();

        try {
            Store store = StoreRepo.searchById(id);
            if (store != null){
                txtStoId.setText(store.getStoId());
                txtCapacity.setText(String.valueOf(store.getCapacity()));
                txtLocation.setText(store.getLocation());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"store is not found !").show();
        }
    }
}
