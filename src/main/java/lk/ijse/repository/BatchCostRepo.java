package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.BatchCost;

import java.sql.Connection;
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
}

