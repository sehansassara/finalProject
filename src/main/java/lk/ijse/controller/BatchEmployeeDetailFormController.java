package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.model.BatchEmployee;
import lk.ijse.model.tm.BatchEmployeeTm;
import lk.ijse.repository.BatchEmployeeRepo;

import java.sql.SQLException;
import java.util.List;

public class BatchEmployeeDetailFormController {

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableView<BatchEmployeeTm> tblBatEmp;

    @FXML
    private TextField txtBatId;

    @FXML
    private TextField txtEMPId;

    public void initialize() {
            setCellValueFactory();
            loadAllBatchEmp();
    }

    private void setCellValueFactory() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
    }

    private void loadAllBatchEmp() {
        ObservableList<BatchEmployeeTm> batEmpde = FXCollections.observableArrayList();
        try {
            List<BatchEmployee> batEmp = BatchEmployeeRepo.getAll();
            for (BatchEmployee batchEmployee1 : batEmp){
                BatchEmployeeTm tmt = new BatchEmployeeTm(
                        batchEmployee1.getEmpId(),
                        batchEmployee1.getBatId()
                );
                batEmpde.add(tmt);
            }
            tblBatEmp.setItems(batEmpde);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }
    }

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

    public void tblBatEmpOnMouse(MouseEvent mouseEvent) {
        int index = tblBatEmp.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String batId = colBatId.getCellData(index).toString();
        String empId = colEmpId.getCellData(index).toString();


        txtBatId.setText(batId);
        txtEMPId.setText(empId);
    }
}
