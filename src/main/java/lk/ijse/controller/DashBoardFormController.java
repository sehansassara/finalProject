package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.model.Ingredient;
import lk.ijse.repository.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DashBoardFormController implements Initializable {

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private PieChart pieChart;


    @FXML
    private BarChart<?, ?> barchart;

    public AnchorPane anchorpaneDash;

    @FXML
    private Label lblCusCount;


    @FXML
    private Label lblEmpCount;

    @FXML
    private Label lblOrderCount;

    @FXML
    private Label lblSupCount;

    @FXML
    void btnBatchCostOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/batchCost_form.fxml"));


        anchorpaneDash.getChildren().clear();
        anchorpaneDash.getChildren().add(dashboardPane);
    }
    

    @FXML
    void btnIngredient1OnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/batchIngredientDetail_form.fxml"));


        anchorpaneDash.getChildren().clear();
        anchorpaneDash.getChildren().add(dashboardPane);

    }
    

    @FXML
    void btnOrderPlace1OnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int customerCount;
        int employeeCount;
        int orderCount;
        int supplierCount;

        try {
            iniLineChart();
            iniPieChart();
            iniBarChart();
            customerCount = CustomerRepo.getCustomerCount();
            employeeCount = EmployeeRepo.getEmployeeCount();
            orderCount = OrderRepo.getOrderCount();
            supplierCount = SupplierRepo.getSupplierCount();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setCustomerCount(customerCount);
        setEmployeeCount(employeeCount);
        setOrderCount(orderCount);
        setSupplierCount(supplierCount);
    }

    private void setCustomerCount(int customerCount) {
        lblCusCount.setText(String.valueOf(customerCount));
    }

    private void setSupplierCount(int supplierCount) {
        lblSupCount.setText(String.valueOf(supplierCount));
    }

    private void setOrderCount(int orderCount) {
        lblOrderCount.setText(String.valueOf(orderCount));
    }

    private void setEmployeeCount(int employeeCount) {
        lblEmpCount.setText(String.valueOf(employeeCount));
    }

    private void iniLineChart() throws SQLException {
        XYChart.Series series = new XYChart.Series();

        Map<String, Double> paymentsByDay = PaymentRepo.getPaymentsByDay();

        for (Map.Entry<String, Double> entry : paymentsByDay.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);
    }


    private void iniPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            List<Ingredient> ingredients = IngredientRepo.getAll();

            for (Ingredient ingredient : ingredients) {
                pieChartData.add(new PieChart.Data(ingredient.getType(), ingredient.getUnitPrice()));
            }

            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void iniBarChart() {
        XYChart.Series series = new XYChart.Series();
        try {
            Map<String, Integer> ordersByDay = OrderRepo.getOrdersByDay();

            for (Map.Entry<String, Integer> entry : ordersByDay.entrySet()) {
                series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
            }

            barchart.getData().add(series);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
