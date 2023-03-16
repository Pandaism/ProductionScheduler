package net.safefleet.prod.productionscheduler.fx;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

/**
 * FormattedTableCell is an abstract class that extends TableCell.
 * It is used to provide custom formatting for table cells, including
 * adjusting font size to fit the cell width.
 *
 * @param <S> The type of the elements contained within the TableView.
 * @param <T> The type of the elements represented by this TableColumn.
 */
public abstract class FormattedTableCell<S, T> extends TableCell<S, T> {
    // Declare a Text object and the maximum font size
    protected final Text text;
    private final double MAX_FONT_SIZE = 32;

    /**
     * Constructor for the FormattedTableCell class.
     */
    public FormattedTableCell() {
        // Initialize the Text object and set its properties
        this.text = new Text();
        this.text.setFont(Font.font(MAX_FONT_SIZE));
        this.text.setStyle("-fx-fill: #fff");

        // Set the TableCell's graphic and content display properties
        this.setGraphic(this.text);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * Sets formatted text in the TableCell by adjusting the font size
     * to fit the cell width.
     *
     * @param textList A list of strings to display in the TableCell.
     * @param column The TableColumn associated with the TableCell.
     */
    protected void setFormattedText(List<String> textList, TableColumn column) {
        // Check if the textList is not null
        if (textList != null) {
            // Set the text in the TableCell
            this.text.setText(String.join("\n", textList));

            // Calculate the width of the text and the TableColumn
            double textWidth = this.text.getBoundsInLocal().getWidth();
            double colWidth = column.getWidth();

            // Adjust the font size if the text width exceeds the column width
            if (textWidth > colWidth) {
                double fontSize = 32 * colWidth / textWidth;
                fontSize = Math.max(fontSize, 8);
                this.text.setFont(Font.font(fontSize - 1));
            }
        }
    }
}
