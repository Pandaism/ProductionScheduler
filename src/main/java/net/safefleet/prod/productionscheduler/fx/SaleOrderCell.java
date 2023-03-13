package net.safefleet.prod.productionscheduler.fx;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.safefleet.prod.productionscheduler.data.SalesOrderData;

public class SaleOrderCell extends TableCell<SalesOrderData, String> {
    private final Text text;

    public SaleOrderCell() {
        this.text = new Text();
        this.text.setFont(Font.font(32));
        this.text.setStyle("-fx-fill: #fff;");
        this.setGraphic(this.text);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(item != null || !empty) {
            if(item != null) {
                if(item.contains("SO")) {
                    this.text.setText(item.substring(2));
                } else {
                    this.text.setText(item);
                }
            }

            double textWidth = this.text.getBoundsInLocal().getWidth();
            double colWidth = this.getTableColumn().getWidth();
            if(textWidth > colWidth) {
                double newFontSize = 32 * colWidth / textWidth;
                newFontSize = Math.max(newFontSize, 8);
                this.text.setFont(Font.font(newFontSize - 1));
            }
        } else {
            this.text.setText(null);
        }
    }

}
