package work;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import work.model.Item;
import work.model.ItemListWrapper;
import work.view.ItemEditDialogController;
import work.view.ItemOverviewController;
import work.view.RootLayoutController;
import work.view.StatisticsController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Item> itemData = FXCollections.observableArrayList();

    public MainApp() {
        itemData.add(new Item("food"));
        itemData.add(new Item("car"));
        itemData.add(new Item("house"));
    }

    public ObservableList<Item> getItemData() {

        return itemData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WorkForceCost");

        // Устанавливаем иконку приложения.
        //this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        initRootLayout();

        showItemOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //пытаемся загрузить последний открытый файл
        File file = getItemFilePath();
        if (file != null) {
            loadItemDataFromFile(file);
        }
    }

    public void showItemOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ItemOverview.fxml"));
            AnchorPane itemOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(itemOverview);

            ItemOverviewController controller = loader.getController();

            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного адресата.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте адресата и возвращается значение true.
     *
     * @param item - объект адресата, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showItemEditDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ItemEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //создаем диалоговое окно
            Stage dialogStge = new Stage();
            dialogStge.setTitle("Edit");
            //dialogStge.getIcons().add(new Image("file:resources/images/icon.png"));
            dialogStge.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            dialogStge.initModality(Modality.WINDOW_MODAL);
            dialogStge.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStge.setScene(scene);

            ItemEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStge);
            controller.setItem(item);

            dialogStge.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //возвращение последнего открытого файла
    public File getItemFilePath() {
        Preferences pefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = pefs.get("filePath",null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setItemFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Обновление заглавия сцены.
            primaryStage.setTitle("WorkForceCost - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Обновление заглавия сцены.
            primaryStage.setTitle("WorkForceCost");
        }
    }

    //загружает данные из указанного файла
    public void loadItemDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ItemListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            ItemListWrapper wrapper = (ItemListWrapper) um.unmarshal(file);

            itemData.clear();
            itemData.addAll(wrapper.getItems());
            //сохраняем путь к файлу
            setItemFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    //сохранение информации в файл
    public void saveItemDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ItemListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            ItemListWrapper wrapper = new ItemListWrapper();
            wrapper.setItems(itemData);
            m.marshal(wrapper,file);
            setItemFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void showStatistics() {
        try {
            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Statistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Statistics");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаёт элементы в контроллер.
            StatisticsController controller = loader.getController();
            controller.setItemData(itemData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
