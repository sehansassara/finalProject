package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Ingredient;
import lk.ijse.model.OrderDetail;
import lk.ijse.model.Payment;
import lk.ijse.model.batchIngredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class IngredientRepo {
    public static boolean save(Ingredient ingredient) throws SQLException {
        String sql = "INSERT INTO ingredient VALUES( ?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,ingredient.getIngId());
        pstm.setObject(2,ingredient.getType());
        pstm.setObject(3,ingredient.getUnitPrice());
        pstm.setObject(4,ingredient.getQtyOnHand());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Ingredient ingredient) throws SQLException {
        String sql = "UPDATE ingredient SET type = ?, unitPrice = ?, qtyOnHand = ? WHERE ING_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,ingredient.getType());
        pstm.setObject(2,ingredient.getUnitPrice());
        pstm.setObject(3,ingredient.getQtyOnHand());
        pstm.setObject(4,ingredient.getIngId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE ingredient SET status = 'DELETE' WHERE ING_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,id);

        return pstm.executeUpdate() > 0;
    }

    public static Ingredient searchById(String id) throws SQLException {
        String sql = "SELECT * FROM ingredient WHERE ING_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String ingId = resultSet.getString(1);
            String type = resultSet.getString(2);
            double unitPrice = resultSet.getDouble(3);
            String qtyOnHand = resultSet.getString(4);

            Ingredient ingredient = new Ingredient(ingId,type,unitPrice,qtyOnHand);
            return  ingredient;
        }
        return null;
    }

    public static List<Ingredient> getAll() throws SQLException {
        String sql = "SELECT * FROM ingredient WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Ingredient> ingredientList = new ArrayList<>();

        while (resultSet.next()){
            String id = resultSet.getString(1);
            String type = resultSet.getString(2);
            double unitPrice = resultSet.getDouble(3);
            String qtyOnHand = resultSet.getString(4);

            Ingredient ingredient = new Ingredient(id,type,unitPrice,qtyOnHand);
            ingredientList.add(ingredient);
        }
        return ingredientList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT ING_ID FROM ingredient";
        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public static boolean update(List<batchIngredient> bcList) throws SQLException {
        for (batchIngredient bi : bcList) {
            boolean isUpdateQty = updateQty(bi.getBatId(), bi.getQty(),bi.getIngId());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(String batId, int qty, String ingId) throws SQLException {
        String sql = "UPDATE ingredient SET qtyOnHand = qtyOnHand - ? WHERE ING_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, ingId);

        return pstm.executeUpdate() > 0;
    }
}
