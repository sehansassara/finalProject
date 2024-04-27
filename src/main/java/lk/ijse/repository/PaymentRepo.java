package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Order;
import lk.ijse.model.Payment;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepo {
    public static boolean save(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment VALUES( ?,?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,payment.getPayId());
        pstm.setObject(2,payment.getAmount());
        pstm.setObject(3,payment.getDate());
        pstm.setObject(4,payment.getType());
        pstm.setObject(5,payment.getOrdId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Payment payment) throws SQLException {
        String sql = "UPDATE payment SET amount = ?, date = ?, type = ?, ORD_ID = ? WHERE PAY_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,payment.getAmount());
        pstm.setObject(2,payment.getDate());
        pstm.setObject(3,payment.getType());
        pstm.setObject(4,payment.getOrdId());
        pstm.setObject(5,payment.getPayId());

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
            Date date = Date.valueOf(resultSet.getString(3));
            String type = resultSet.getString(4);
            String ordID = resultSet.getString(5);

            Payment payment = new Payment(id,amount,date,type,ordID);
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
            Date date = Date.valueOf(resultSet.getString(3));
            String type = resultSet.getString(4);
            String ordId = resultSet.getString(5);

            Payment payment = new Payment(id,amount,date,type,ordId);
            paymentList.add(payment);
        }
        return paymentList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT PAY_ID FROM payment";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
}
