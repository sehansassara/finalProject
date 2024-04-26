package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Batch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchRepo {
    public static boolean save(Batch batch) throws SQLException {
        String sql = "INSERT INTO batch VALUES (?,?,?,?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,batch.getBatId());
        pstm.setObject(2,batch.getStoId());
        pstm.setObject(3,batch.getPrice());
        pstm.setObject(4,batch.getType());
        pstm.setObject(5,batch.getProductionDate());
        pstm.setObject(6,batch.getNumberOfReject());
        pstm.setObject(7,batch.getQty());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Batch batch) throws SQLException {
        String sql = "UPDATE batch SET STO_ID = ?, price = ?, type = ?, productionDate = ?, numberOfRejectedItem = ?, qty = ? WHERE BAT_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,batch.getStoId());
        pstm.setObject(2,batch.getPrice());
        pstm.setObject(3,batch.getType());
        pstm.setObject(4,batch.getProductionDate());
        pstm.setObject(5,batch.getNumberOfReject());
        pstm.setObject(6,batch.getQty());
        pstm.setObject(7,batch.getBatId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE batch SET status = 'DELETE' WHERE BAT_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,id);

       return pstm.executeUpdate() > 0;
    }

    public static Batch searchById(String id) throws SQLException {
        String sql = "SELECT * FROM batch WHERE BAT_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String batId = resultSet.getString(1);
            String stoId = resultSet.getString(2);
            double price = Double.parseDouble(resultSet.getString(3));
            String type = resultSet.getString(4);
            String productionDate = resultSet.getString(5);
            int numberOfReject = resultSet.getInt(6);
            int qty = resultSet.getInt(7);

            Batch batch = new Batch(batId,stoId,price,type,productionDate,numberOfReject,qty);
            return batch;
        }
        return null;
    }

    public static List<Batch> getAll() throws SQLException {
        String sql = "SELECT * FROM batch WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Batch> batchList = new ArrayList<>();

        while (resultSet.next()){
            String batId = resultSet.getString(1);
            String stoId = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            String type = resultSet.getString(4);
            String productionDate = resultSet.getString(5);
            int numberOfReject = resultSet.getInt(6);
            int qty = resultSet.getInt(7);

            Batch batch = new Batch(batId,stoId,price,type,productionDate,numberOfReject,qty);

            batchList.add(batch);
        }
        return batchList;
    }
}
