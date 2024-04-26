package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.model.Payment;
import lk.ijse.model.tm.OrderTm;
import lk.ijse.model.tm.PaymentTm;
import lk.ijse.repository.PaymentRepo;

import java.sql.SQLException;
import java.util.List;

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
    private TextField txtOrdId;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;


    @FXML
    private TextField txtPayId;

    public void initialize() {
        setCellValueFactory();
        loadAllPayments();

        ObservableList<String> paymentTypes = FXCollections.observableArrayList("Cash", "Card");
        choiceType.setItems(paymentTypes);


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
        txtOrdId.setText("");
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
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        double amount  = 0.0;
        if (!txtAmount.getText().isEmpty()) {
            amount = Double.parseDouble(txtAmount.getText());
        }

        String date = txtDate.getText();
        String choiceTypeValue = (String) choiceType.getValue();
        String ordId = txtOrdId.getId();

        if (payId.isEmpty() || date.isEmpty() || choiceTypeValue == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Payment payment = new Payment(payId,amount,date,choiceTypeValue,ordId);

        try {
            boolean isSaved = PaymentRepo.save(payment);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"payment is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllPayments();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        double amount  = 0.0;
        if (!txtAmount.getText().isEmpty()) {
            amount = Double.parseDouble(txtAmount.getText());
        }

        String date = txtDate.getText();
        String choiceTypeValue = (String) choiceType.getValue();
        String ordId = txtOrdId.getId();

        if (payId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter payment ID.").show();
            return;
        }

        Payment payment = new Payment(payId,amount,date,choiceTypeValue,ordId);

        try {
            boolean isUpdated = PaymentRepo.update(payment);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"payment is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllPayments();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String payId = txtPayId.getText();

        try {
            Payment payment = PaymentRepo.searchById(payId);
            if (payment != null){
                txtPayId.setText(payment.getPayId());
                txtAmount.setText(String.valueOf(payment.getAmount()));
                txtDate.setText(payment.getDate());
                txtOrdId.setText(payment.getOrdId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"payment is not found !").show();
        }
    }

    public void choiceTypeOnAction(MouseEvent mouseEvent) {

    }
}
