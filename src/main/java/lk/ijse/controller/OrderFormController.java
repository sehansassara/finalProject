package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.model.Customer;
import lk.ijse.model.Order;
import lk.ijse.model.Payment;
import lk.ijse.model.tm.OrderTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;
import lk.ijse.repository.PaymentRepo;

import java.sql.SQLException;
import java.util.List;

public class OrderFormController {

    @FXML
    private AnchorPane AnchorpaneOrder;

    @FXML
    private TableColumn<?, ?> colCustId;

    @FXML
    private TableColumn<?, ?> colDateOfPlace;

    @FXML
    private TableColumn<?, ?> colDateOfRelease;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableView<OrderTm> tblOrder;

    @FXML
    private JFXComboBox<String> comCustId;

    @FXML
    private JFXComboBox<String> comPayId;

    @FXML
    private TextField txtDateOfPlace;

    @FXML
    private TextField txtDateOfRelease;

    @FXML
    private TextField txtOrderId;

    public void initialize(){
        setCellValueFactory();
        loadAllOrders();
        getCustomerIds();
        getPaymentIds();
    }

    private void getPaymentIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = PaymentRepo.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comPayId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCustomerIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();


        try {
            List<String> idList = CustomerRepo.getIds();
            for (String id : idList){
                obList.add(id);
            }
            comCustId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("ordId"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colDateOfPlace.setCellValueFactory(new PropertyValueFactory<>("dateOfPlace"));
        colPayId.setCellValueFactory(new PropertyValueFactory<>("payId"));
        colDateOfRelease.setCellValueFactory(new PropertyValueFactory<>("dateOfRelease"));
    }

    private void loadAllOrders() {
        ObservableList<OrderTm> obList = FXCollections.observableArrayList();

        try {
            List<Order> orderList = OrderRepo.getAll();
            for (Order order : orderList){
                OrderTm tm = new OrderTm(
                        order.getOrdId(),
                        order.getCusId(),
                        order.getDateOfPlace(),
                        order.getPayId(),
                        order.getDateOfRelease()
                );
                obList.add(tm);
            }

            tblOrder.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtOrderId.setText("");
        txtDateOfPlace.setText("");
        txtDateOfRelease.setText("");
        comCustId.setValue(null);
        comPayId.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtOrderId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter order ID for delete.").show();
            return;
        }


        try {
            boolean isDeleted = OrderRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "order is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllOrders();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String ordId = txtOrderId.getText();
        String cusId = comCustId.getValue();
        String dop = txtDateOfPlace.getText();
        String payId = comPayId.getValue();
        String dor = txtDateOfRelease.getText();

        if (ordId.isEmpty() || cusId == null || dop.isEmpty() || payId == null || dor.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Order order = new Order(ordId,cusId,dop,payId,dor);

        try {
            boolean isSaved = OrderRepo.save(order);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"order is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();

        }
        loadAllOrders();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String ordId = txtOrderId.getText();
        String cusId = comCustId.getValue();
        String dop = txtDateOfPlace.getText();
        String payId = comPayId.getValue();
        String dor = txtDateOfRelease.getText();

        if (ordId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter order ID.").show();
            return;
        }


        Order order = new Order(ordId,cusId,dop,payId,dor);
        try {
            boolean isUpdated = OrderRepo.update(order);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"order is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllOrders();
    }

    @FXML
    void comCusIdOnAction(ActionEvent event) {
        String id = comCustId.getValue();

        try {
            Customer customer = CustomerRepo.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comPayIdOnAction(ActionEvent event) {
        String payId = comPayId.getValue();

        try {
            Payment payment = PaymentRepo.searchById(payId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void txtSearchOnAction(ActionEvent actionEvent) {
        String id = txtOrderId.getText();

        try {
            Order order = OrderRepo.searchById(id);
            if (order != null){
                txtOrderId.setText(order.getOrdId());
                comCustId.setValue(order.getCusId());
                txtDateOfPlace.setText(order.getDateOfPlace());
                comPayId.setValue(order.getPayId());
                txtDateOfRelease.setText(order.getDateOfRelease());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"order is not found !").show();
        }
    }
}
