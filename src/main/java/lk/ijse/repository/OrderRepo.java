package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepo {
    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders VALUES( ?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, order.getOrdId());
        pstm.setObject(2, order.getCusId());
        pstm.setObject(3, order.getDateOfPlace());

        return pstm.executeUpdate() > 0;
    }

  /*  public static boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET CUS_ID = ?, dateOfPlace = ? WHERE ORD_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, order.getCusId());
        pstm.setObject(2, order.getDateOfPlace());
        pstm.setObject(3, order.getOrdId());

        return pstm.executeUpdate() > 0;
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

        if (resultSet.next()) {
            String ordId = resultSet.getString(1);
            String cusId = resultSet.getString(2);
            Date dateOfPlase = Date.valueOf(resultSet.getString(3));


            Order order = new Order(ordId, cusId, dateOfPlase);

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

        while (resultSet.next()) {
            String ordId = resultSet.getString(1);
            String cusId = resultSet.getString(2);
            Date dop = Date.valueOf(resultSet.getString(3));


            Order order = new Order(ordId, cusId, dop);
            orderList.add(order);
        }
        return orderList;
    }*/

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT ORD_ID FROM orders";
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

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT ORD_ID FROM orders ORDER BY ORD_ID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }

    public static int getOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS order_count FROM orders  WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("order_count");
        }
        return 0;
    }

    public static Map<String, Integer> getOrdersByDay() throws SQLException {
        Map<String, Integer> ordersByDay = new HashMap<>();

        String sql = "SELECT dateOfPlace, COUNT(*) AS order_count FROM orders GROUP BY dateOfPlace";
        try (PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery()) {

            while (resultSet.next()) {
                String orderDay = resultSet.getString("dateOfPlace");
                int orderCount = resultSet.getInt("order_count");
                ordersByDay.put(orderDay, orderCount);
            }
        }

        return ordersByDay;
    }
}
