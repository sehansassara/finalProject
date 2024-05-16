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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.controller.Util.Regex;
import lk.ijse.model.Batch;
import lk.ijse.model.BatchEmployee;
import lk.ijse.model.Employee;
import lk.ijse.model.Store;
import lk.ijse.model.tm.BatchTm;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.BatchEmployeeRepo;
import lk.ijse.repository.BatchRepo;
import lk.ijse.repository.EmployeeRepo;
import lk.ijse.repository.StoreRepo;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BatchFormController {

    @FXML
    private TextField txtBatId;
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
    private Label lblBatId;

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
        getCurrentBatchIds();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));

        ObservableList<String> batchType = FXCollections.observableArrayList("Jack Mackerel", "Tuna Mackeral","Mackerel","Sardin");
        choiceType.setItems(batchType);
    }

    private void getCurrentBatchIds() {
        try {
            String currentId = BatchRepo.getCurrentId();

            String nextBatchId = generateNexrBatchId(currentId);
            txtBatId.setText(nextBatchId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrBatchId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("B");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "B" + String.format("%03d", ++idNum);
        }
        return "B001";
    }

    private void loadAllEmp() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList){
                final Employee emp = employee;

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

                        // Update the existing EmployeeTm object in the obList
                        tm.setBtnSave(new JFXButton("Added"));
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
        choiceType.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtBatId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter batch ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
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
        clearFields();
        getCurrentBatchIds();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = comStoId.getValue();

        double price = 0.0;
        if (!txtPrice.getText().isEmpty()) {
            price = Double.parseDouble((txtPrice.getText()));
        }

        String type = (String) choiceType.getValue();

        Date productionDate = null;
        if (!txtProductionDate.getText().isEmpty()){
             productionDate = Date.valueOf(LocalDate.now());
        }
        int numOfReject = 0;
        if (!txtNumOfReject.getText().isEmpty()) {
            numOfReject = Integer.parseInt(txtNumOfReject.getText());
        }

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        if (id.isEmpty() || stoId == null|| type == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }
    Batch batch = new Batch(id, stoId, price, type, productionDate, numOfReject, qty);

    try {
        boolean isSaved = BatchRepo.save(batch);
        if (isSaved) {
            new Alert(Alert.AlertType.CONFIRMATION, "batch is saved").show();
        }
    } catch (SQLException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }

        loadAllBatch();
        clearFields();
        getCurrentBatchIds();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtBatId.getText();
        String stoId = comStoId.getValue();
        double price = 0.0;
        if (!txtPrice.getText().isEmpty()) {
            price = Double.parseDouble((txtPrice.getText()));
        }
        String type = (String) choiceType.getValue();

        Date productionDate = null;
        if (!txtProductionDate.getText().isEmpty()){
            productionDate = Date.valueOf(LocalDate.now());
        }


        int numOfReject = 0;
        if (!txtNumOfReject.getText().isEmpty()) {
            numOfReject = Integer.parseInt(txtNumOfReject.getText());
        }

        int qty = 0;
        if (!txtQty.getText().isEmpty()) {
            qty = Integer.parseInt(txtQty.getText());
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

    Batch batch = new Batch(id, stoId, price, type, productionDate, numOfReject, qty);

    try {
        boolean isUpdated = BatchRepo.update(batch);
        if (isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "batch is updated").show();
        }
    } catch (SQLException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
        loadAllBatch();
        clearFields();
        getCurrentBatchIds();
        txtProductionDate.setText(String.valueOf(LocalDate.now()));
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
                txtProductionDate.setText(String.valueOf(batch.getProductionDate()));
                txtNumOfReject.setText(String.valueOf(batch.getNumberOfReject()));
                txtQty.setText(String.valueOf(batch.getQty()));
                choiceType.setValue(batch.getType());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"batch is not found !").show();
        }
    }

    public void tblBatOnClick(MouseEvent mouseEvent) {
        int index = tblBatch.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String batId = colBatId.getCellData(index).toString();
        String stoId = colStoId.getCellData(index).toString();
        String unitPrice = colPrice.getCellData(index).toString();
        String type = colType.getCellData(index).toString();
        String productionDate = colProductionDate.getCellData(index).toString();
        String numOfReject = colNumberOfRejectedItem.getCellData(index).toString();
        String qty = colQty.getCellData(index).toString();

        txtBatId.setText(batId);
        comStoId.setValue(stoId);
        txtPrice.setText(unitPrice);
        choiceType.setValue(type);
        txtProductionDate.setText(productionDate);
        txtNumOfReject.setText(numOfReject);
        txtQty.setText(qty);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtBatId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtPrice)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQty)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtNumOfReject)) return false;

        return true;
    }

    public void txtBatIdOnKeyReleased(javafx.scene.input.KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtBatId);
    }

    @FXML
    void txtPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtPrice);
    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQty);
    }


    @FXML
    void txtNumOfRejectOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtNumOfReject);
    }

}