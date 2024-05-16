package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.controller.Util.Regex;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Payment;
import lk.ijse.model.tm.PaymentTm;
import lk.ijse.repository.OrderRepo;
import lk.ijse.repository.PaymentRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFormController {

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colOrd;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<PaymentTm> tblPayment;

    @FXML
    private JFXComboBox<String> comOrd;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtPayId;

    public void initialize() {
        setCellValueFactory();
        loadAllPayments();
        getOrdIds();
        getCurrentPayIds();
        ObservableList<String> paymentTypes = FXCollections.observableArrayList("Cash", "Card");
        choiceType.setItems(paymentTypes);
        txtDate.setText(String.valueOf(LocalDate.now()));
        comOrd.setEditable(true);

    }

    private void getCurrentPayIds() {
        try {
            String currentId = PaymentRepo.getCurrentId();

            String nextCusId = generateNexrPayId(currentId);
            txtPayId.setText(nextCusId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrPayId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("P");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "P" + String.format("%03d", ++idNum);
        }
        return "P001";
    }

    private void getOrdIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();


        try {
            List<String> idList = OrderRepo.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comOrd.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("payId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colOrd.setCellValueFactory(new PropertyValueFactory<>("ordId"));
    }

    private void loadAllPayments() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try {
            List<Payment> paymentList = PaymentRepo.getAll();
            for (Payment payment : paymentList){
                PaymentTm tm = new PaymentTm(
                        payment.getPayId(),
                        payment.getAmount(),
                        payment.getDate(),
                        payment.getType(),
                        payment.getOrdId()
                );
                obList.add(tm);
            }

            tblPayment.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtPayId.setText("");
        txtAmount.setText("");
        txtDate.setText("");
        comOrd.setValue(null);
        choiceType.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        if (payId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter payment ID.").show();
            return;
        }

        try {
            boolean isDeleted = PaymentRepo.delete(payId);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"payment is Deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllPayments();
        clearFields();
        getCurrentPayIds();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        double amount  = 0.0;
        if (!txtAmount.getText().isEmpty()) {
            amount = Double.parseDouble(txtAmount.getText());
        }

        Date date = null;
        if (!txtDate.getText().isEmpty()){
            date = Date.valueOf(LocalDate.now());
        }

        String choiceTypeValue = (String) choiceType.getValue();
        String ordId = comOrd.getValue();

        if (payId.isEmpty() || choiceTypeValue == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Payment payment = new Payment(payId, amount, date, choiceTypeValue, ordId);

        try {
            boolean isSaved = PaymentRepo.save(payment);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "payment is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllPayments();
        clearFields();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        double amount  = 0.0;
        if (!txtAmount.getText().isEmpty()) {
            amount = Double.parseDouble(txtAmount.getText());
        }

        Date date = null;
        if (!txtDate.getText().isEmpty()){
            date = Date.valueOf(LocalDate.now());
        }
        
        String choiceTypeValue = (String) choiceType.getValue();
        String ordId = comOrd.getValue();

        if (payId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter payment ID.").show();
            return;
        }

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        Payment payment = new Payment(payId, amount, date, choiceTypeValue, ordId);

        try {
            boolean isUpdated = PaymentRepo.update(payment);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "payment is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllPayments();
        clearFields();
        getCurrentPayIds();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        try {
            Payment payment = PaymentRepo.searchById(payId);
            if (payment != null){
                txtPayId.setText(payment.getPayId());
                txtAmount.setText(String.valueOf(payment.getAmount()));
                txtDate.setText(String.valueOf(payment.getDate()));
                comOrd.setValue(payment.getOrdId());
                choiceType.setValue(payment.getType());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"payment is not found !").show();
        }
    }

    public void choiceTypeOnAction(MouseEvent mouseEvent) {

    }

    public void tblPayOnMouse(MouseEvent mouseEvent) {
        int index = tblPayment.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String payId = colPayId.getCellData(index).toString();
        String amount = colAmount.getCellData(index).toString();
        String date = colDate.getCellData(index).toString();
        String type = colType.getCellData(index).toString();
        String ordId = colOrd.getCellData(index).toString();

        txtPayId.setText(payId);
        txtAmount.setText(amount);
        txtDate.setText(date);
        choiceType.setValue(type);
        comOrd.setValue(ordId);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtPayId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtAmount)) return false;
        return true;
    }
    @FXML
    void txtAmountOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtAmount);
    }

    @FXML
    void txtPayIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtPayId);
    }

    @FXML
    void btnBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Payment.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("payId",txtPayId.getText());
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport,data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void filterOrderId(KeyEvent event) {
        ObservableList<String > filterCon = FXCollections.observableArrayList();
        String enteredText = comOrd.getEditor().getText();

        try {
            List<String> conList = OrderRepo.getIds();

            for (String con : conList){
                if (con.contains(enteredText)){
                    filterCon.add(con);
                }
            }
            comOrd.setItems(filterCon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void orderIdMousedClick(MouseEvent event) {
        comOrd.getSelectionModel().clearSelection();
    }
}
