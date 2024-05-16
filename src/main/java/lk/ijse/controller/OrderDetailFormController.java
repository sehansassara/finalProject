package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.OrderDetail;
import lk.ijse.repository.OrderDetailRepo;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailFormController {

    @FXML
    private TableColumn<?, ?> colBatId;

    @FXML
    private TableColumn<?, ?> colOrdId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableView<OrderDetail> tblOrderDetail;

    public  void initialize() {
        loadAllOrderDetails();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colOrdId.setCellValueFactory(new PropertyValueFactory<>("ordId"));
        colBatId.setCellValueFactory(new PropertyValueFactory<>("batId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void loadAllOrderDetails() {
        ObservableList<OrderDetail> obList = FXCollections.observableArrayList();

        try {
            List<OrderDetail> orderList = OrderDetailRepo.getAll();
            for (OrderDetail orderDetail : orderList){
                OrderDetail tm = new OrderDetail(
                        orderDetail.getOrdId(),
                        orderDetail.getBatId(),
                        orderDetail.getQty()
                );
                obList.add(tm);
            }

            tblOrderDetail.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
