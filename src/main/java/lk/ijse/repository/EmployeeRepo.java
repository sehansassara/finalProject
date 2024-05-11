package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee VALUES (?,?,?,?,?,?,?,'ACTIVE')";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,employee.getEmpId());
        pstm.setObject(2,employee.getFirstName());
        pstm.setObject(3,employee.getLastName());
        pstm.setObject(4,employee.getAddress());
        pstm.setObject(5,employee.getTel());
        pstm.setObject(6,employee.getSalary());
        pstm.setObject(7,employee.getPosition());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET firstName = ?, lastName = ?, address = ?, tel = ?, salary = ?, position = ? WHERE EMP_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,employee.getFirstName());
        pstm.setObject(2,employee.getLastName());
        pstm.setObject(3,employee.getAddress());
        pstm.setObject(4,employee.getTel());
        pstm.setObject(5,employee.getSalary());
        pstm.setObject(6,employee.getPosition());
        pstm.setObject(7,employee.getEmpId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "UPDATE employee SET status = 'DELETE' WHERE EMP_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1,id);

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EMP_ID = ?";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String empId = resultSet.getString(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String address = resultSet.getString(4);
            String tel = resultSet.getString(5);
            double salary = resultSet.getDouble(6);
            String position = resultSet.getString(7);

            Employee employee = new Employee(empId,firstName,lastName,address,tel,salary,position);
            return  employee;
        }
        return null;
    }

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().
                getConnection().
                prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> employeeList = new ArrayList<>();

        while (resultSet.next()){
            String empId = resultSet.getString(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String address = resultSet.getString(4);
            String tel = resultSet.getString(5);
            double salary = resultSet.getDouble(6);
            String position = resultSet.getString(7);

            Employee employee = new Employee(empId,firstName,lastName,address,tel,salary,position);

            employeeList.add(employee);
        }
        return employeeList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT EMP_ID FROM employee ORDER BY EMP_ID DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String empId = resultSet.getString(1);
            return empId;
        }
        return null;
    }

    public static int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS employee_count FROM employee WHERE status = 'ACTIVE'";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("employee_count");
        }
        return 0;
    }
}
