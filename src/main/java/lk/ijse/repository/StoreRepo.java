package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Payment;
import lk.ijse.model.Store;

import java.security.cert.CertStoreSpi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreRepo {
    public static boolean save(Store store) throws SQLException {
        String sql = "INSERT INTO store VALUES(?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,store.getStoId());
        pstm.setObject(2,store.getCapacity());
        pstm.setObject(3,store.getLocation());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Store store) throws SQLException {
        String sql = "UPDATE store SET capacity = ?, location = ? WHERE STO_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,store.getCapacity());
        pstm.setObject(2,store.getLocation());
        pstm.setObject(3,store.getStoId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE store SET status = 'DELETE' WHERE STO_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

                pstm.setObject(1,id);

        return pstm.executeUpdate() > 0;
    }

    public static Store searchById(String id) throws SQLException {
        String sql = "SELECT * FROM store WHERE STO_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String stoId = resultSet.getString(1);
            int capacity = resultSet.getInt(2);
            String location = resultSet.getString(3);

            Store store = new Store(stoId,capacity,location);
            return store;
        }
        return null;
    }

    public static List<Store> getAll() throws SQLException {
        String sql = "SELECT * FROM store WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Store> storeList = new ArrayList<>();

        while (resultSet.next()){
            String id = resultSet.getString(1);
            int capacity = resultSet.getInt(2);
            String location = resultSet.getString(3);

            Store store = new Store(id,capacity,location);
            storeList.add(store);
        }
        return storeList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT STO_ID FROM store";
        PreparedStatement pstm = DbConnection.getInstance().
              getConnection().
              prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String id = resultSet.getString(1);

            idList.add(id);
        }
        return idList;
    }
}
