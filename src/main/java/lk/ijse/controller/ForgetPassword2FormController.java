package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.controller.Util.Regex;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgetPassword2FormController implements Initializable {

    @FXML
    private Label lblMain;

    @FXML
    private AnchorPane anchorpaneForget2;
    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnConfirm;
    @FXML
    private Label lblOtp;

    @FXML
    private Label lblStatus;
    @FXML
    private TextField txt1;

    @FXML
    private TextField txt2;

    @FXML
    private TextField txt3;

    @FXML
    private TextField txt4;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblOtp.setText(String.valueOf(ForgetPassword1FormController.OTP));
        Platform.runLater(() -> txt1.requestFocus());
        txt1.setOnKeyReleased(event -> moveFocus(txt1, txt2));
        txt2.setOnKeyReleased(event -> moveFocus(txt2, txt3));
        txt3.setOnKeyReleased(event -> moveFocus(txt3, txt4));
    }

    private void moveFocus(TextField currentTextField, TextField nextTextField) {
        if (!currentTextField.getText().isEmpty()) {
            nextTextField.requestFocus();
        }
    }
    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane loginPane = FXMLLoader.load(this.getClass().getResource("/view/forgetPassword1_form.fxml"));

        Scene scene = new Scene(loginPane);

        Stage stage = (Stage) anchorpaneForget2.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnConfirmOnAction(ActionEvent event) throws IOException {
        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        String otp1 = txt1.getText();
        String otp2 = txt2.getText();
        String otp3 = txt3.getText();
        String otp4 = txt4.getText();

        String setOtp = otp1 + otp2 + otp3 + otp4;




        if (lblOtp.getText().equalsIgnoreCase(setOtp)){
            lblStatus.setStyle("-fx-fill: green");
            lblStatus.setText("Correct OTP");

            Parent root = FXMLLoader.load(getClass().getResource("/view/forgetPassword3_form.fxml"));
            Scene scene = btnConfirm.getScene();
            root.translateXProperty().set(scene.getWidth());

            AnchorPane parentContainer = (AnchorPane) scene.getRoot();
            parentContainer.getChildren().add(root);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(event1 -> {
                parentContainer.getChildren().removeAll(anchorpaneForget2);
            });
            timeline.play();
        } else {
            lblStatus.setStyle("-fx-fill: red");
            lblStatus.setText("Invalid OTP");
        }
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt1)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt2)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt3)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt4)) return false;
        return true;
    }

    @FXML
    void txt1KeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt1);
    }

    @FXML
    void txt2KeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt2);
    }

    @FXML
    void txt3KeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt3);
    }

    @FXML
    void txt4KeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txt4);

    }
}
