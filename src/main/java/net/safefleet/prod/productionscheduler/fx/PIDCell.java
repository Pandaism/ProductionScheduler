package net.safefleet.prod.productionscheduler.fx;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.safefleet.prod.productionscheduler.data.SalesOrder;

import java.util.List;
import java.util.stream.Collectors;

public class PIDCell extends TableCell<SalesOrder, String> {
    private final Text text;

    public PIDCell() {
        this.text = new Text();
        this.text.setFont(Font.font(32));
        this.text.setStyle("-fx-fill: #fff;");
        this.setGraphic(this.text);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        int index = getIndex();
        if (index >= 0 && index < getTableView().getItems().size()) {
            SalesOrder so = getTableView().getItems().get(index);
            List<String> pids = so.getPartsList().stream().map(parts -> parts.getPid().trim()).collect(Collectors.toList());
            this.text.setText(String.join("\n", pids));

            double textWidth = this.text.getBoundsInLocal().getWidth();
            double colWidth = this.getTableColumn().getWidth();
            if(textWidth > colWidth) {
                double fontSize = 32 * colWidth / textWidth;
                // Set a minimum font size of 8 to ensure that the text remains visible
                fontSize = Math.max(fontSize, 8);
                this.text.setFont(Font.font(fontSize - 1));
            }
        } else {
            this.text.setText(null);
        }
    }

}
