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
import javafx.scene.layout.AnchorPane;
import lk.ijse.model.Customer;
import lk.ijse.model.Order;
import lk.ijse.model.tm.CustomerTm;
import lk.ijse.model.tm.OrderTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;

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
    private TextField txtCustId;

    @FXML
    private TextField txtDateOfPlace;

    @FXML
    private TextField txtDateOfRelease;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtPayId;

    public void initialize(){
        setCellValueFactory();
        loadAllOrders();
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
        txtCustId.setText("");
        txtDateOfPlace.setText("");
        txtPayId.setText("");
        txtPayId.setText("");
        txtDateOfRelease.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtOrderId.getText();

        try {
            boolean isDeleted = OrderRepo.delete(id);
            new Alert(Alert.AlertType.CONFIRMATION,"order is deleted").show();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllOrders();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String ordId = txtOrderId.getText();
        String cusId = txtCustId.getText();
        String dop = txtDateOfPlace.getText();
        String payId = txtPayId.getText();
        String dor = txtDateOfRelease.getText();

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
        String cusId = txtCustId.getText();
        String dop = txtDateOfPlace.getText();
        String payId = txtPayId.getText();
        String dor = txtDateOfRelease.getText();

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

    public void txtSearchOnAction(ActionEvent actionEvent) {
        String id = txtOrderId.getText();

        try {
            Order order = OrderRepo.searchById(id);
            if (order != null){
                txtOrderId.setText(order.getOrdId());
                txtCustId.setText(order.getCusId());
                txtDateOfPlace.setText(order.getDateOfPlace());
                txtPayId.setText(order.getPayId());
                txtDateOfRelease.setText(order.getDateOfRelease());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"order is not found !").show();
        }
    }
}
