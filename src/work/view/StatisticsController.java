package work.view;

import java.util.Arrays;
import java.util.List;
import work.model.Item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

public class StatisticsController {
    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> itemsNames = FXCollections.observableArrayList();

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл был загружен.
     */
    @FXML
    private void initialize(List<Item> items) {
        String[] strings =new String[items.size()];

        int i=0;
        // Получаем массив
        for (Item p : items) {
            strings[i]=p.getItemOfExp();
            i++;
        }
        // Преобразуем его в список и добавляем в наш ObservableList
        itemsNames.addAll(Arrays.asList(strings));

        // Назначаем имена для горизонтальной оси.
        xAxis.setCategories(itemsNames);
    }

    public void setItemData(List<Item> items) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (Item p : items) {
            series.getData().add(new XYChart.Data<>(p.getItemOfExp(), p.getCostsAtMonth()));
        }

        barChart.getData().add(series);
    }
}
