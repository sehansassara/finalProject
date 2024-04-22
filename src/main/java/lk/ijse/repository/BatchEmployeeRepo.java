package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.BatchEmployee;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchEmployeeRepo {

    public static boolean save(BatchEmployee batchEmployee) throws SQLException {
        String sql = "INSERT INTO batchEmployeeDetail  VALUES (?,?)";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,batchEmployee.getBatId());
        pstm.setObject(2,batchEmployee.getEmpId());

        return pstm.executeUpdate() > 0;
    }
}
