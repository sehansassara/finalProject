package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.model.Ingredient;
import lk.ijse.model.Payment;
import lk.ijse.model.tm.IngredientTm;
import lk.ijse.model.tm.PaymentTm;
import lk.ijse.repository.IngredientRepo;
import lk.ijse.repository.PaymentRepo;

import java.sql.SQLException;
import java.util.List;

public class IngredientFormController {

    @FXML
    private ChoiceBox<String> choiceType;

    @FXML
    private TableColumn<?, ?> colIngId;

    @FXML
    private TableColumn<?, ?> colQuantityOnHand;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<IngredientTm> tblIngredient;

    @FXML
    private TextField txtIngId;

    @FXML
    private TextField txtQuantityOnHand;

    @FXML
    private TextField txtUnitPrice;
    public void initialize() {
        setCellValueFactory();
        loadAllIngredient();

        ObservableList<String> ingredientType = FXCollections.observableArrayList("Fish", "Salt","Chemicals","Tin");
        choiceType.setItems(ingredientType);
    }

    private void setCellValueFactory() {
        colIngId.setCellValueFactory(new PropertyValueFactory<>("ingId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantityOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private void loadAllIngredient() {
        ObservableList<IngredientTm> obList = FXCollections.observableArrayList();
        try {
            List<Ingredient> ingredientList = IngredientRepo.getAll();
            for (Ingredient ingredient : ingredientList){
                IngredientTm tm = new IngredientTm(
                        ingredient.getIngId(),
                        ingredient.getType(),
                        ingredient.getUnitPrice(),
                        ingredient.getQtyOnHand()
                );
                obList.add(tm);
            }
            tblIngredient.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtIngId.setText("");
        txtUnitPrice.setText("");
        txtQuantityOnHand.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtIngId.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter ingredient ID.").show();
            return;
        }

        try {
            boolean isDeleted = IngredientRepo.delete(id);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"ingredient is deleted").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllIngredient();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtIngId.getText();
        String type = (String) choiceType.getValue();

        double price = 0.0;
        if (!txtUnitPrice.getText().isEmpty()) {
            price = Double.parseDouble(txtUnitPrice.getText());
        }

        String qtyOnHand = txtQuantityOnHand.getText();

        if (id.isEmpty() || type == null || qtyOnHand.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        Ingredient ingredient = new Ingredient(id,type,price,qtyOnHand);
        try {
            boolean isSaved = IngredientRepo.save(ingredient);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"ingredient is saved").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllIngredient();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtIngId.getText();
        String type = (String) choiceType.getValue();

        double price = 0.0;
        if (!txtUnitPrice.getText().isEmpty()) {
            price = Double.parseDouble(txtUnitPrice.getText());
        }

        String qtyOnHand = txtQuantityOnHand.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter ingredient ID.").show();
            return;
        }

        Ingredient ingredient = new Ingredient(id,type,price,qtyOnHand);

        try {
            boolean isUpdated = IngredientRepo.update(ingredient);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"ingredient is updated").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllIngredient();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtIngId.getText();

        try {
            Ingredient ingredient = IngredientRepo.searchById(id);
            if (ingredient != null){
                txtIngId.setText(ingredient.getIngId());
                txtUnitPrice.setText(String.valueOf(ingredient.getUnitPrice()));
                txtQuantityOnHand.setText(ingredient.getQtyOnHand());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
