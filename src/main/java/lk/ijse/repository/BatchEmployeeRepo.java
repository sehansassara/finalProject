package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Batch;
import lk.ijse.model.BatchEmployee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchEmployeeRepo {

    public static boolean save(BatchEmployee batchEmployee) throws SQLException {
        String sql = "INSERT INTO batchEmployeeDetail  VALUES (?,?)";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, batchEmployee.getBatId());
        pstm.setObject(2, batchEmployee.getEmpId());

        return pstm.executeUpdate() > 0;
    }

    public static List<BatchEmployee> getAll() throws SQLException {
        String sql = "SELECT * " +
                "FROM batch b " +
                "INNER JOIN batchEmployeeDetail be ON b.BAT_ID = be.BAT_ID " +
                "INNER JOIN employee e ON be.EMP_ID = e.EMP_ID " +
                "ORDER BY b.BAT_ID ASC";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<BatchEmployee> batchEmpList = new ArrayList<>();

        while (resultSet.next()) {
            String empId = resultSet.getString("BAT_ID");
            String batId = resultSet.getString("EMP_ID");

            BatchEmployee batchEmp = new BatchEmployee(batId,empId);
            batchEmpList.add(batchEmp);
        }
        return batchEmpList;
    }

    public static boolean delete(BatchEmployee batchEmployee) throws SQLException {
        String sql = "DELETE FROM batchEmployeeDetail WHERE EMP_ID = ? AND BAT_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1, batchEmployee.getBatId());
        pstm.setString(2, batchEmployee.getEmpId());


           return pstm.executeUpdate() > 0;
    }
}