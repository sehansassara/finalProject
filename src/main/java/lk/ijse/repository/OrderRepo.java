package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;
import lk.ijse.model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo {
    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES( ?,?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
               getConnection().
               prepareStatement(sql);

        pstm.setObject(1,order.getOrdId());
        pstm.setObject(2,order.getCusId());
        pstm.setObject(3,order.getDateOfPlace());
        pstm.setObject(4,order.getPayId());
        pstm.setObject(5,order.getDateOfRelease());

       return pstm.executeUpdate() > 0;
    }

    public static boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET CUS_ID = ?, dateOfPlace = ?, PAY_ID = ?, dateOfRelease = ? WHERE ORD_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,order.getCusId());
        pstm.setObject(2,order.getDateOfPlace());
        pstm.setObject(3,order.getPayId());
        pstm.setObject(4,order.getDateOfRelease());
        pstm.setObject(5,order.getOrdId());

        return  pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE orders set status = 'DELETE' WHERE ORD_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static Order searchById(String id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE ORD_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String ordId = resultSet.getString(1);
            String cusId = resultSet.getString(2);
            String dateOfPlase = resultSet.getString(3);
            String payId = resultSet.getString(4);
            String dateOfRelease = resultSet.getString(5);

            Order order = new Order(ordId,cusId,dateOfPlase,payId,dateOfRelease);

            return order;
        }
    return null;
    }

    public static List<Order> getAll() throws SQLException {
        String sql = "SELECT * FROM orders WHERE status = 'ACTIVE'";
        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Order> orderList = new ArrayList<>();

        while (resultSet.next()){
            String ordId = resultSet.getString(1);
            String cusId = resultSet.getString(2);
            String dop = resultSet.getString(3);
            String payId = resultSet.getString(4);
            String dor = resultSet.getString(5);

            Order order = new Order(ordId,cusId,dop,payId,dor);
            orderList.add(order);
        }
        return orderList;
    }
}
