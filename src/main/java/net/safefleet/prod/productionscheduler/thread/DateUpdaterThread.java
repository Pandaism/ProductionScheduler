package net.safefleet.prod.productionscheduler.thread;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DateUpdaterThread is a custom Runnable class for updating a Label with the
 * current date range. This class provides a mechanism to display the current
 * week's date range (from Monday to Friday) in the provided Label.
 */
public class DateUpdaterThread implements Runnable {
    // Logger for logging information and errors
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUpdaterThread.class);
    // Label to be updated with the date information
    private final Label label;

    /**
     * Constructor takes a Label as an argument.
     *
     * @param label The Label to be updated with the date range.
     */
    public DateUpdaterThread(Label label) {
        this.label = label;
    }

    /**
     * The run method of the Runnable interface, which will be executed when the
     * thread is started. This method implements the logic for updating the
     * provided Label with the current date range.
     */
    @Override
    public void run() {
        // Get the current date
        LocalDate now = LocalDate.now();
        // Get the Monday of the current week
        LocalDate monday = now.with(DayOfWeek.MONDAY);
        // Get the Friday of the current week
        LocalDate friday = now.with(DayOfWeek.FRIDAY);
        // Define a date formatter
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Set the font of the label
        this.label.setFont(new Font("Arial", 26));
        // Update the label text with the formatted date range
        this.label.setText("Production Schedule for " + df.format(monday) + " to " + df.format(friday));

        // Log the date range update
        LOGGER.info("Advancing week {} to {}", df.format(monday), df.format(friday));
    }
}
