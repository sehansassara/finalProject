package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashBoardFormController {

    @FXML
    private AnchorPane anchorpaneDash;

    @FXML
    void btnBatch1OnAction(ActionEvent event) {

    }

    @FXML
    void btnCustomer1OnAction(ActionEvent event) {
    }



    @FXML
    void btnBatchCostOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/batchCost_form.fxml"));


        anchorpaneDash.getChildren().clear();
        anchorpaneDash.getChildren().add(dashboardPane);
    }

    @FXML
    void btnEmployee1OnAction(ActionEvent event) throws IOException {

    }

    @FXML
    void btnIngredient1OnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/batchIngredientDetail_form.fxml"));


        anchorpaneDash.getChildren().clear();
        anchorpaneDash.getChildren().add(dashboardPane);

    }

    @FXML
    void btnOrder1OnAction(ActionEvent event) {

    }

    @FXML
    void btnOrderPlace1OnAction(ActionEvent event) {

    }

    @FXML
    void btnSupplier1OnAction(ActionEvent event) {

    }

}
