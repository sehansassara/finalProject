package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardFormController {

    @FXML
    private AnchorPane Anchorpane1;

    @FXML
    private AnchorPane Anchorpane2;

    @FXML
    private JFXButton btnCustomer;

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));


        Anchorpane2.getChildren().clear();
        Anchorpane2.getChildren().add(dashboardPane);
    }

}
