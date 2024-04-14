package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Order;
import lk.ijse.model.Payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepo {
    public static boolean save(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment VALUES( ?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,payment.getPayId());
        pstm.setObject(2,payment.getAmount());
        pstm.setObject(3,payment.getDate());
        pstm.setObject(4,payment.getType());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Payment payment) throws SQLException {
        String sql = "UPDATE payment SET amount = ?, date = ?, type = ? WHERE PAY_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,payment.getAmount());
        pstm.setObject(2,payment.getDate());
        pstm.setObject(3,payment.getType());
        pstm.setObject(4,payment.getPayId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String payId) throws SQLException {
        String sql = "UPDATE payment SET status = 'DELETE' WHERE PAY_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,payId);

        return pstm.executeUpdate() > 0;
    }

    public static Payment searchById(String payId) throws SQLException {
        String sql = "SELECT * FROM payment WHERE PAY_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, payId);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String id = resultSet.getString(1);
            double amount = resultSet.getDouble(2);
            String date = resultSet.getString(3);
            String type = resultSet.getString(4);

            Payment payment = new Payment(id,amount,date,type);
            return  payment;
        }
        return null;
    }

    public static List<Payment> getAll() throws SQLException {
        String sql = "SELECT * FROM payment WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Payment> paymentList = new ArrayList<>();

        while (resultSet.next()){
            String id = resultSet.getString(1);
            double amount = resultSet.getDouble(2);
            String date = resultSet.getString(3);
            String type = resultSet.getString(4);

            Payment payment = new Payment(id,amount,date,type);
            paymentList.add(payment);
        }
        return paymentList;
    }
}
