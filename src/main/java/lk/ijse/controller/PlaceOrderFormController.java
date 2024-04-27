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
import javafx.scene.layout.AnchorPane;
import lk.ijse.model.Batch;
import lk.ijse.model.Customer;
import lk.ijse.model.tm.OrderTm;
import lk.ijse.repository.BatchRepo;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PlaceOrderFormController {
    @FXML
    private AnchorPane AnchorpaneOrder;

    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colCusId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private JFXComboBox<String> comBatId;

    @FXML
    private JFXComboBox<String> comCustName;

    @FXML
    private Label lblBatQty;

    @FXML
    private Label lblCustId;

    @FXML
    private Label lblDop;

    @FXML
    private Label lblOrdId;

    @FXML
    private Label lblType;

    @FXML
    private Label lblUnitPrice;
    
    @FXML
    private Label lblNetTotal;

    @FXML
    private TableView<OrderTm> tblCart;

    @FXML
    private TextField txtQty;
    private ObservableList<OrderTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        setDate();
        getCurrentOrderIds();
        getCustomerNames();
        getBatchIds();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }

    private void getBatchIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> batIdList = BatchRepo.getIds();

            for (String id : batIdList) {
                obList.add(id);
            }
            comBatId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void getCustomerNames() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> nameList = CustomerRepo.getNames();

            for (String name : nameList) {
                obList.add(name);
            }

            comCustName.setItems(obList);

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void getCurrentOrderIds() {
        try {
            String currentId = OrderRepo.getCurrentId();

            String nextOrderId = generateNexrOrderId(currentId);
            lblOrdId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrOrderId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("O");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "O" + String.format("%03d", ++idNum);
        }
        return "O001";
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDop.setText(String.valueOf(now));
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String batId = comBatId.getValue();
        String cusId = lblCustId.getText();
        String type = lblType.getText();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = qty * unitPrice;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> types = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(types.orElse(no) == yes) {
                int selectedIndex = tblCart.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblCart.refresh();
                calculateNetTotal();
            }
        });

        for (int i = 0;i<tblCart.getItems().size(); i++){
            if (batId.equals(colBatId.getCellData(i))) {

                OrderTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblCart.refresh();

                calculateNetTotal();
                return;
            }
        }

        OrderTm tm = new OrderTm(batId, cusId, type, unitPrice, qty, total, btnRemove);
        obList.add(tm);

        tblCart.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");

    }

    private void calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < tblCart.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }
    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

    }

    @FXML
    void comBatchIdOnAction(ActionEvent event) {
        String batId = comBatId.getValue();

        try {
            Batch batch = BatchRepo.searchById(batId);
            if(batch != null) {
                lblUnitPrice.setText(String.valueOf(batch.getPrice()));
                lblType.setText(String.valueOf(batch.getType()));
                lblBatQty.setText(String.valueOf(batch.getQty()));
            }

            txtQty.requestFocus();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comCustNameOnAction(ActionEvent event) {
        String name = comCustName.getValue();
        try {
            Customer customer = CustomerRepo.searchByName(name);

            lblCustId.setText(customer.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToCartOnAction(event);
    }

}












/*package lk.ijse.controller;

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

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PlaceOrderFormController {

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
    }

    private void loadAllOrders() {
        ObservableList<OrderTm> obList = FXCollections.observableArrayList();

        try {
            List<Order> orderList = OrderRepo.getAll();
            for (Order order : orderList){
                OrderTm tm = new OrderTm(
                        order.getOrdId(),
                        order.getCusId(),
                        order.getDateOfPlace()
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
        comCustId.setValue(null);
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
        Date dop = Date.valueOf(txtDateOfPlace.getText());


        if (ordId.isEmpty() || cusId == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Order order = new Order(ordId,cusId,dop);

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
        Date dop = Date.valueOf(txtDateOfPlace.getText());

        if (ordId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter order ID.").show();
            return;
        }


        Order order = new Order(ordId,cusId,dop);
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
                txtDateOfPlace.setText(String.valueOf(order.getDateOfPlace()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.INFORMATION,"order is not found !").show();
        }
    }
}*/
