package work.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    private final StringProperty itemOfExp;//название статьи расходов
    private final IntegerProperty costsAtAll;//траты за все время
    private final IntegerProperty costsAtMonth;//траты в месяц

    public Item() {
        this(null);
    }

    public Item(String itemOfExp) {
        this.itemOfExp = new SimpleStringProperty(itemOfExp);
        this.costsAtAll = new SimpleIntegerProperty(560000);
        this.costsAtMonth = new SimpleIntegerProperty(560000/300);
    }

    public String getItemOfExp() {
        return itemOfExp.get();
    }

    public StringProperty itemOfExpProperty() {
        return itemOfExp;
    }

    public int getCostsAtAll() {
        return costsAtAll.get();
    }

    public IntegerProperty costsAtAllProperty() {
        return costsAtAll;
    }

    public int getCostsAtMonth() {
        return costsAtMonth.get();
    }

    public IntegerProperty costsAtMonthProperty() {
        return costsAtMonth;
    }

    public void setItemOfExp(String itemOfExp) {
        this.itemOfExp.set(itemOfExp);
    }

    public void setCostsAtAll(int costsAtAll) {
        this.costsAtAll.set(costsAtAll);
    }

    public void setCostsAtMonth(int costsAtMonth) {
        costsAtMonth = this.getCostsAtAll()/300;
        this.costsAtMonth.set(costsAtMonth);
    }
}
