package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
public class LoginFormController {

    public AnchorPane rootnode;
    public TextField emailField;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Hyperlink linkForgetPassword;

    @FXML
    private Hyperlink linkNewAccount;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserId;


    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String userId = txtUserId.getText();
        String pw = txtPassword.getText();

        try {
            checkCredential(userId, pw);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void checkCredential(String userId, String pw) throws SQLException, IOException {
        String sql = "SELECT userId, password FROM user WHERE userId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userId);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String dbPw = resultSet.getString("password");

            if(pw.equals(dbPw)) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "sorry! password is incorrect!").show();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "sorry! user id can't be find!").show();
        }



    }
    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(getClass().getResource("/view/mainBoard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) rootnode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("main Board Form");
    }

    @FXML
    void linkForgetPasswordOnAction(ActionEvent event) throws IOException, MessagingException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/forgetPassword2_form.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Password Forget Form");
        stage.show();


      /*  String recipientEmail = emailField.getText();


        String otp = generateOTP();


        sendEmail(recipientEmail, otp);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);


        alert.setTitle("Success");


        alert.setHeaderText("OTP sent successfully");


        alert.setContentText("We have sent a 4-digit OTP to your email address. Please check your inbox.");


        alert.showAndWait();


    }


    private String generateOTP() {


        int otpLength = 4;


        String allowedChars = "0123456789";


        Random random = new Random();


        StringBuilder otp = new StringBuilder(otpLength);


        for (int i = 0; i < otpLength; i++) {


            int index = random.nextInt(allowedChars.length());


            otp.append(allowedChars.charAt(index));


        }


        return otp.toString();


    }


    private void sendEmail(String recipientEmail, String otp) throws MessagingException {


        String smtpHost = "smtp.gmail.com";


        int smtpPort = 465;


        String senderEmail = "sehansassara2002@gmail.com";


        String senderPassword = "Sehan0731";


        Properties properties = new Properties();


        properties.put("mail.smtp.auth", "true");


        properties.put("mail.smtp.starttls.enable", "true");


        properties.put("mail.smtp.host", smtpHost);


        properties.put("mail.smtp.port", smtpPort);


        properties.put("mail.smtp.ssl.enable", "true");


        Session session = Session.getInstance(properties, new Authenticator() {


            @Override


            protected PasswordAuthentication getPasswordAuthentication() {


                return new PasswordAuthentication(senderEmail, senderPassword);


            }


        });


        try {


            Message message = new MimeMessage(session);


            message.setFrom(new InternetAddress(senderEmail));


            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));


            message.setSubject("Password Reset OTP");


            message.setText("Your OTP for password reset is: " + otp);


            Transport.send(message);


            System.out.println("Email sent successfully!");


        } catch (MessagingException e) {


            e.printStackTrace();


            throw e;


        }*/
    }

    @FXML
    void linkNewAccountOnAction(ActionEvent event) {

    }

}