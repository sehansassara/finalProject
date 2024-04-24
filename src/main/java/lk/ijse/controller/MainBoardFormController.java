package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainBoardFormController {

    public AnchorPane Anchorpanemain;
    @FXML
    private AnchorPane Anchorpane1;

    @FXML
    private AnchorPane Anchorpane2;

    @FXML
    private JFXButton btnCustomer;


    @FXML
    private JFXButton btnDashboard;

    public void initialize() throws IOException {
        loadDashboardForm();
       // showNotification();
    }

    private void loadDashboardForm() throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/dashBoard_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane customerPane = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(customerPane);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/dashBoard_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(dashboardPane);
    }
    @FXML
    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/order_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }
    @FXML
    public void btnBatchOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/batch_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }
    @FXML
    public void btnIngredientOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/ingredient_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }
    @FXML
    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/employee_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }
    @FXML
    public void btnStoreOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/store_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }
    @FXML
    public void btnPaymentOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/payment_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/supplier_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(orderPane);
    }
    @FXML
    public void btnBatchEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/batchEmployeeDetail_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(dashboardPane);
    }
    @FXML
    public void btnBatchIngredientOnAction(ActionEvent actionEvent) {
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/dashBoard_form.fxml"));


        Anchorpanemain.getChildren().clear();
        Anchorpanemain.getChildren().add(dashboardPane);
    }
    @FXML
    void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane loginPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(loginPane);

        Stage stage = (Stage) Anchorpane1.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
