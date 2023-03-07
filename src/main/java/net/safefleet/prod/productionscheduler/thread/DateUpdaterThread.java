package net.safefleet.prod.productionscheduler.thread;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUpdaterThread implements Runnable {
    private final Label label;

    public DateUpdaterThread(Label label) {
        this.label = label;
    }

    @Override
    public void run() {
        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(DayOfWeek.MONDAY);
        LocalDate friday = now.with(DayOfWeek.FRIDAY);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        this.label.setFont(new Font("Arial", 26));
        this.label.setText("Production Schedule for " + df.format(monday) + " to " + df.format(friday));
    }
}
