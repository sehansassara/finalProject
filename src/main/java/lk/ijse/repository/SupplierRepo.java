package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {
    public static boolean save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier VALUES (?,?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,supplier.getSupId());
        pstm.setObject(2,supplier.getCompanyName());
        pstm.setObject(3,supplier.getAddress());
        pstm.setObject(4,supplier.getContact());
        pstm.setObject(5,supplier.getIngId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updated(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET companyName = ?, address = ?, contact = ?, ING_ID = ? WHERE SUP_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,supplier.getCompanyName());
        pstm.setObject(2,supplier.getAddress());
        pstm.setObject(3,supplier.getContact());
        pstm.setObject(4,supplier.getIngId());
        pstm.setObject(5,supplier.getSupId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE supplier SET status = 'DELETE' WHERE SUP_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

            pstm.setObject(1,id);

        return pstm.executeUpdate() > 0;
    }

    public static Supplier searchById(String id) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE SUP_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){
            String supId = resultSet.getString(1);
            String companyName = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact = resultSet.getString(4);
            String ingId = resultSet.getString(5);

            Supplier supplier = new Supplier(supId,companyName,address,contact,ingId);
            return supplier;
        }
        return null;
    }

    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM supplier WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supplierList = new ArrayList<>();

        while (resultSet.next()){
            String supId = resultSet.getString(1);
            String companyName = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact = resultSet.getString(4);
            String ingId = resultSet.getString(5);

            Supplier supplier = new Supplier(supId,companyName,address,contact,ingId);
            supplierList.add(supplier);
        }
        return supplierList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT SUP_ID FROM supplier ORDER BY SUP_ID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String supId = resultSet.getString(1);
            return supId;
        }
        return null;
    }

    public static int getSupplierCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS supplier_count FROM supplier  WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("supplier_count");
        }
        return 0;
    }
}
