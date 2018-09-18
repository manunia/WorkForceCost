package work.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import work.MainApp;
import work.model.Item;

import java.util.Optional;

public class ItemOverviewController {
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item,String> itemColumn;
    @FXML
    private TableColumn<Item,Integer> atAllColumn;
    @FXML
    private TableColumn<Item,Integer> monthColumn;

    @FXML
    private Label itemLabel;
    @FXML
    private Label atAllLabel;
    @FXML
    private Label monthLabel;
    @FXML
    private Label total;

    private MainApp mainApp;

    public ItemOverviewController() {
    }

    @FXML
    private void initialize() {
        //инициализация таблицы
        itemColumn.setCellValueFactory(cellData->cellData.getValue().itemOfExpProperty());
        atAllColumn.setCellValueFactory(cellData->cellData.getValue().costsAtAllProperty().asObject());
        monthColumn.setCellValueFactory(cellData->cellData.getValue().costsAtMonthProperty().asObject());
        //очистка дополнительной информации
        showItemDetails(null);
        //слушатель изменения выбора
        itemTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showItemDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        itemTable.setItems(mainApp.getItemData());
    }

    private void showItemDetails(Item item) {
        if (item != null) {
            itemLabel.setText(item.getItemOfExp());
            atAllLabel.setText(Integer.toString(item.getCostsAtAll()));
            monthLabel.setText(Integer.toString(item.getCostsAtMonth()));
            //выводим значение общих среднемесячных расходов
            int t = 0;
            for (Item i:mainApp.getItemData()) {
                total.setText(Integer.toString(t += i.getCostsAtMonth()));
            }

        } else {
            itemLabel.setText("");
            atAllLabel.setText("");
            monthLabel.setText("");
        }
    }

    //метод удаления
    @FXML
    private void handleDeleteItem() {
        int selectedIndex = itemTable.getSelectionModel().getSelectedIndex();
        String selectedItem = itemTable.getSelectionModel().getSelectedItem().getItemOfExp();
        if (selectedIndex >= 0) {
            //вызов диалога для подтверждения удаления
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete dialog");
            alert.setHeaderText("Deleting " + selectedItem);
            alert.setContentText("Are you sure you want to delete " + selectedItem + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                itemTable.getItems().remove(selectedIndex);
            } else {
                return;
            }
        } else {
            //если ничего не выбрано
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No item selected");
            alert.setContentText("Please select an item in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewItem() {
        Item tempItem = new Item();
        boolean okClicked = mainApp.showItemEditDialog(tempItem);
        if (okClicked) {
            mainApp.getItemData().add(tempItem);
        }
    }

    @FXML
    private void handleEditItem() {
        Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            boolean okClicked = mainApp.showItemEditDialog(selectedItem);
            if (okClicked) {
                showItemDetails(selectedItem);
            }
        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select an item in the table.");

            alert.showAndWait();
        }
    }
}
