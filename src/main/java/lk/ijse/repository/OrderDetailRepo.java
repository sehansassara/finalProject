package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(OrderDetail od) throws SQLException {
        String sql = "INSERT INTO orderDetail VALUES(?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, od.getOrdId());
        pstm.setString(2, od.getBatId());
        pstm.setInt(3, od.getQty());

        return pstm.executeUpdate() > 0;    //false ->  |
    }

    public static List<OrderDetail> getAll() throws SQLException {
        String sql = "SELECT ORD_ID, BAT_ID, qty FROM orderDetail";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<OrderDetail> orderDetailList = new ArrayList<>();

        while (resultSet.next()) {
            String ordId = resultSet.getString("ORD_ID");
            String batId = resultSet.getString("BAT_ID");
            int qty = Integer.parseInt(resultSet.getString("qty"));



            OrderDetail ba = new OrderDetail(ordId,batId,qty);
            orderDetailList.add(ba);
        }
        return orderDetailList;
    }
}
