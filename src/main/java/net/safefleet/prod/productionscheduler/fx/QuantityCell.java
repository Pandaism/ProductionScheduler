package net.safefleet.prod.productionscheduler.fx;

import javafx.scene.control.*;
import javafx.scene.text.Font;
import net.safefleet.prod.productionscheduler.data.SalesOrder;

import java.util.List;
import java.util.stream.Collectors;

public class QuantityCell extends TableCell<SalesOrder, Integer> {
    private final Label label;

    public QuantityCell() {
        this.label = new Label();
        this.label.setFont(Font.font(32));
        this.setGraphic(this.label);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        int index = getIndex();
        if (index >= 0 && index < getTableView().getItems().size()) {
            SalesOrder so = getTableView().getItems().get(index);
            List<Integer> quantities = so.getPartsList().stream().map(SalesOrder.Parts::getQuantity).collect(Collectors.toList());
            this.label.setText(quantities.stream().map(Object::toString).collect(Collectors.joining("\n")));

        } else {
            this.label.setText(null);
        }
    }
}
