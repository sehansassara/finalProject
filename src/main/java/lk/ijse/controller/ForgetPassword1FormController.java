package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lombok.SneakyThrows;

import java.util.Random;

public class ForgetPassword1FormController {
    @FXML
    public AnchorPane AnchorpaneForget;
    @FXML
    private JFXButton btnSendOtp;

    @FXML
    private Label lblEmail;
    public static int OTP;
    @SneakyThrows
    @FXML
    void btnSendOtpOnAction(ActionEvent event) {
        int otp = new Random().nextInt(9000) + 1000;
        JavaMail.sendMail("lasithasumudu.ls@gmail.com",otp);
        OTP=otp;
        System.out.println(">>>"+otp);

        Parent root = FXMLLoader.load(getClass().getResource("/view/forgetPassword2_form.fxml"));
        Scene scene = btnSendOtp.getScene();
        root.translateXProperty().set(scene.getWidth());

        AnchorPane parentContainer = (AnchorPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(AnchorpaneForget);
        });
        timeline.play();
    }

}
