package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.controller.Util.Regex;
import lk.ijse.model.Ingredient;
import lk.ijse.model.tm.IngredientTm;
import lk.ijse.repository.IngredientRepo;

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
        getCurrentIngIds();

        ObservableList<String> ingredientType = FXCollections.observableArrayList("Fish", "Salt","Chemicals","Tin");
        choiceType.setItems(ingredientType);
    }

    private void getCurrentIngIds() {
        try {
            String currentId = IngredientRepo.getCurrentId();

            String nextCusId = generateNexrIngId(currentId);
            txtIngId.setText(nextCusId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexrIngId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("I");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "I" + String.format("%03d", ++idNum);
        }
        return "I001";
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
        clearFields();
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

        if (isValied()) {
    Ingredient ingredient = new Ingredient(id, type, price, qtyOnHand);
    try {
        boolean isSaved = IngredientRepo.save(ingredient);
        if (isSaved) {
            new Alert(Alert.AlertType.CONFIRMATION, "ingredient is saved").show();
        }
    } catch (SQLException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
    }
        loadAllIngredient();
        clearFields();
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
if (isValied()) {
    Ingredient ingredient = new Ingredient(id, type, price, qtyOnHand);

    try {
        boolean isUpdated = IngredientRepo.update(ingredient);
        if (isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "ingredient is updated").show();
        }
    } catch (SQLException e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
    }
}
        loadAllIngredient();
        clearFields();
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
                choiceType.setValue(ingredient.getType());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void tblIngOnMouse(MouseEvent mouseEvent) {
        int index = tblIngredient.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String ingId = colIngId.getCellData(index).toString();
        String type = colType.getCellData(index).toString();
        String unitPrice = colUnitPrice.getCellData(index).toString();
        String qtyOnHand = colQuantityOnHand.getCellData(index).toString();

        txtIngId.setText(ingId);
        choiceType.setValue(type);
        txtUnitPrice.setText(unitPrice);
        txtUnitPrice.setText(qtyOnHand);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtIngId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtUnitPrice)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQuantityOnHand)) return false;
        return true;
    }
    @FXML
    void txtIngIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtIngId);
    }

    @FXML
    void txtQuantityOnHandOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.QTY,txtQuantityOnHand);
    }

    @FXML
    void txtUnitPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.Util.TextField.PRICE,txtUnitPrice);
    }
}
