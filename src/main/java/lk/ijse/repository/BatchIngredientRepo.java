package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.batchIngredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchIngredientRepo {
    public static boolean save(List<batchIngredient> bcList) throws SQLException {
        for (batchIngredient bi : bcList) {
            boolean isSaved = save(bi);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(batchIngredient bi) throws SQLException {
        String sql = "INSERT INTO batchIngredientDetail VALUES(?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, bi.getBatId());
        pstm.setInt(2, bi.getQty());
        pstm.setString(3, bi.getIngId());

        return pstm.executeUpdate() > 0;    //false ->  |
    }

    public static List<batchIngredient> getAll() throws SQLException {

        String sql = "SELECT BAT_ID, qty, ING_ID FROM batchIngredientDetail";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<batchIngredient> batchIngredientList = new ArrayList<>();

        while (resultSet.next()) {
            String batId = resultSet.getString("BAT_ID");
            int qty = Integer.parseInt(resultSet.getString("qty"));
            String ingId = resultSet.getString("ING_ID");


            batchIngredient ba = new batchIngredient(batId,qty,ingId);
            batchIngredientList.add(ba);
        }
        return batchIngredientList;
    }
}
