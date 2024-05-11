package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.PlaceOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceOrderRepo {

    public static boolean placeOrder(PlaceOrder po) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

    try {
        boolean isOrderSaved = OrderRepo.save(po.getOrder());
            if (isOrderSaved) {
                 boolean isQtyUpdated = BatchRepo.update(po.getOdList());
            if (isQtyUpdated){
                 boolean isOrderDetailSaved = OrderDetailRepo.
                         save(po.getOdList());
            if (isOrderDetailSaved){
                connection.commit();
                return true;
            }
        }
    }
            connection.rollback();
            return false;
        }catch (Exception e){
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }

    public static String calculateNetTotal(String orderId) throws SQLException {
        double netTotal = 0.0;

            String sql = "SELECT SUM(b.price * od.qty) " +
                    "FROM batch b " +
                    "JOIN orderDetail od ON b.BAT_ID = od.BAT_ID " +
                    "WHERE od.ORD_ID = ?";


            try (PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
                statement.setString(1, orderId);


                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        double c = resultSet.getDouble(1);
                        netTotal=netTotal+c;
                    }
                    return String.valueOf((netTotal));
                }
            }
    }
}

