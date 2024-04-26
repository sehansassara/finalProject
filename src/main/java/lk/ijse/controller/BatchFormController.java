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
import lk.ijse.model.tm.BatchTm;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchFormController {
    @FXML
    private TableColumn<?, ?> colAction11;

    @FXML
    private TableView<BatchTm> tblBatch;

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colNumberOfRejectedItem;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProductionDate;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colStoId;

    @FXML
    private TableColumn<?, ?> colType;


    @FXML
    private TextField txtPrice;

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

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableView<EmployeeTm> tblEmp;

    private ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();



    public void initialize() {
        setCellValueFactory();
        setCellValueFactoryEmp();
        loadAllBatch();
        loadAllEmp();
        getStoreIds();

        ObservableList<String> batchType = FXCollections.observableArrayList("Jack Mackerel", "Tuna Mackeral","Mackerel","Sardin");
        choiceType.setItems(batchType);
    }

    private void loadAllEmp() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList){
                JFXButton btn = new JFXButton("Add");
                btn.setCursor(Cursor.HAND);

                EmployeeTm tm = new EmployeeTm(
                        employee.getEmpId(),
                        employee.getFirstName(),
                        employee.getAddress(),
                        employee.getTel(),
                        employee.getSalary(),
                        employee.getPosition(),
                        btn
                );
                obList.add(tm);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Add?", yes, no).showAndWait();

                    if(type.orElse(no) == yes) {
                        int selectedIndex = obList.indexOf(tm);

                        Employee emp = employeeList.get(selectedIndex);
                        String Empid = emp.getEmpId();
                        String batchId=txtBatId.getText();

                        BatchEmployee batchEmployee = new BatchEmployee(Empid, batchId);

                        try {
                            boolean isSaved = BatchEmployeeRepo.save(batchEmployee);
                            if (isSaved){
                                new Alert(Alert.AlertType.CONFIRMATION,"employee is saved").show();
                            }
                        } catch (SQLException ex) {
                            new Alert(Alert.AlertType.ERROR,ex.getMessage()).show();
                        }

                        obList.remove(tm);
                        obList.add(new EmployeeTm(
                                emp.getEmpId(),
                                emp.getFirstName(),
                                emp.getAddress(),
                                emp.getTel(),
                                emp.getSalary(),
                                emp.getPosition(),
                                new JFXButton("Added")
                        ));
                    }
                });
            }

            tblEmp.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setCellValueFactoryEmp() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnSave"));

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

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colStoId.setCellValueFactory(new PropertyValueFactory<>("stoId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
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
                        batch.getPrice(),
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
        txtPrice.setText("");
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
        double price = Double.parseDouble(txtPrice.getText());
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

        if (id.isEmpty() || stoId.isEmpty() || type == null || productionDate.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Batch batch = new Batch(id,stoId,price,type,productionDate,numOfReject,qty);

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
        double price = Double.parseDouble(txtPrice.getText());
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

        Batch batch = new Batch(id,stoId,price,type,productionDate,numOfReject,qty);

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
                txtPrice.setText(String.valueOf(batch.getPrice()));
                txtProductionDate.setText(batch.getProductionDate());
                txtNumOfReject.setText(String.valueOf(batch.getNumberOfReject()));
                txtQty.setText(String.valueOf(batch.getQty()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"batch is not found !").show();
        }
    }

}