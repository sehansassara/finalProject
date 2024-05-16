package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.BatchCost;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchCostRepo {
    public static boolean placeCost(BatchCost bc) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isIngredientUpdate = IngredientRepo.update(bc.getBcList());
            if (isIngredientUpdate) {
                boolean isBatchIngredientSave = BatchIngredientRepo.save(bc.getBcList());
                if (isBatchIngredientSave) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }


    public static String calculateNetTotal(String batId) {
        double netTotal = 0.0;

        String sql = "SELECT SUM(bid.qty * i.unitPrice) AS batch_cost FROM batchIngredientDetail bid JOIN ingredient i ON bid.ING_ID = i.ING_ID JOIN batch b ON bid.BAT_ID = b.BAT_ID WHERE b.BAT_ID = ?";

        try (PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, batId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    netTotal = resultSet.getDouble("batch_cost");
                }
                return String.valueOf(netTotal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

