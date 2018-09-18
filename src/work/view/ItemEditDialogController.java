package work.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import work.model.Item;

public class ItemEditDialogController {

    @FXML
    private TextField itemNameField;
    @FXML
    private TextField costsAtAllField;
    @FXML
    private TextField costsAtMonthField;

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
        costsAtAllField.setText(Integer.toString(item.getCostsAtAll()));
        costsAtMonthField.setText(Integer.toString(item.getCostsAtMonth()));
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    //если пользователь нажал Ок
    @FXML
    private void handleOk() {
        //проверяем корректность ввода
        if (isInputValid()) {
            item.setItemOfExp(itemNameField.getText());
            //проверяем в какое поле введено новое значение
            if (isChanged()) {
                item.setCostsAtAll(Integer.parseInt(costsAtMonthField.getText()) * 300);
                item.setCostsAtMonth(Integer.parseInt(costsAtMonthField.getText()));
            } else {
                item.setCostsAtAll(Integer.parseInt(costsAtAllField.getText()));
                item.setCostsAtMonth(Integer.parseInt(costsAtMonthField.getText()));
            }
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
        if (costsAtAllField.getText() == null || costsAtAllField.getText().length() == 0
                || costsAtMonthField.getText() == null || costsAtMonthField.getText().length() == 0) {
            errorMessage += "No valid cost!\n";
        } else {
            //пытаемся преобразовать введенное значение в int
            try {
                Integer.parseInt(costsAtAllField.getText());
                Integer.parseInt(costsAtMonthField.getText());
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

    //проверяем изменения в текстовое поле затрат за месяц
    private boolean isChanged() {
        if (item.getCostsAtMonth() != Integer.parseInt(costsAtMonthField.getText())) {
            return true;
        } else return false;
    }
}
