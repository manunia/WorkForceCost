package work.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import work.model.Item;

public class ItemEditDialogController {

    @FXML
    private TextField itemNameField;
    @FXML
    private TextField coctsAtAllField;
    @FXML
    private Label costsAtMonthLabel;

    private Stage dialogStage;
    private Item item;
    private boolean okClicked = false;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }
    //устанавливаем сцену для данного окна диалога
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    //задаем информацию о статье расходов
    public void setItem(Item item) {
        this.item = item;

        itemNameField.setText(item.getItemOfExp());
        coctsAtAllField.setText(Integer.toString(item.getCostsAtAll()));
        costsAtMonthLabel.setText(Integer.toString(item.getCostsAtMonth()));
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    //если пользователь нажал Ок
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            item.setItemOfExp(itemNameField.getText());
            item.setCostsAtAll(Integer.parseInt(coctsAtAllField.getText()));
            item.setCostsAtMonth(Integer.parseInt(costsAtMonthLabel.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    //проверка пользовательского ввода
    private boolean isInputValid() {
        String errorMessage = "";

        if (itemNameField.getText() == null || itemNameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (coctsAtAllField.getText() == null || coctsAtAllField.getText().length() == 0) {
            errorMessage += "No valid cost!\n";
        } else {
            //пытаемся преобразовать введенное значение в int
            try {
                Integer.parseInt(coctsAtAllField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid cost (must be an integer)!\n";
            }
        }

        if (errorMessage.length() == 0) return true;
        else {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
