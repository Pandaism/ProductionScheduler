package net.safefleet.prod.productionscheduler.controllers;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.safefleet.prod.productionscheduler.thread.AutoScrollingThread;
import net.safefleet.prod.productionscheduler.thread.DateUpdaterThread;
import net.safefleet.prod.productionscheduler.thread.ExpandableReaderThread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MainFrameController class is responsible for managing the user interface components
 * and initializing the background threads.
 */
public class MainFrameController {
    // Declare a Label object for the header text
    public Label headerText;

    // Declare ScrollPane objects for the user interface
    public ScrollPane firstScrollbar;
    public ScrollPane secondScrollbar;
    public ScrollPane thirdScrollbar;
    public ScrollPane fourthScrollbar;
    public ScrollPane fifthScrollbar;
    // Declare an HBox object for the centerBox
    public HBox centerBox;

    /**
     * MainFrameController constructor initializes ScrollPane objects.
     */
    public MainFrameController() {
        this.firstScrollbar = new ScrollPane();
        this.secondScrollbar = new ScrollPane();
        this.thirdScrollbar = new ScrollPane();
        this.fourthScrollbar = new ScrollPane();
        this.fifthScrollbar = new ScrollPane();
    }

    /**
     * Apply settings to the ScrollPane objects.
     *
     * @param scrollPane The ScrollPane object to apply settings to.
     */
    private void applyScrollPaneSetting(ScrollPane scrollPane) {
        // Set the scrollPane to fit the content width and height
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        // Set horizontal grow priority to always grow
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
    }

    /**
     * Initialize the controller, apply settings to all ScrollPane objects,
     * and schedule background threads.
     */
    public void initialize() {
        // Apply settings to all ScrollPane objects
        applyScrollPaneSetting(this.firstScrollbar);
        applyScrollPaneSetting(this.secondScrollbar);
        applyScrollPaneSetting(this.thirdScrollbar);
        applyScrollPaneSetting(this.fourthScrollbar);
        applyScrollPaneSetting(this.fifthScrollbar);

        // Create an executor service with a fixed thread pool of size 3
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        // Schedule a DateUpdaterThread to run daily
        executorService.scheduleAtFixedRate(new DateUpdaterThread(this.headerText), 0, 1, TimeUnit.DAYS);
        // Schedule an AutoScrollingThread to run every 5 seconds
        executorService.scheduleAtFixedRate(new AutoScrollingThread(this.firstScrollbar, this.secondScrollbar, this.thirdScrollbar, this.fourthScrollbar, this.fifthScrollbar), 0, 5, TimeUnit.SECONDS);
        // Schedule an ExpandableReaderThread to run every 15 minutes
        executorService.scheduleAtFixedRate(
                new ExpandableReaderThread(
                        this.centerBox, this.firstScrollbar, this.secondScrollbar, this.thirdScrollbar, this.fourthScrollbar, this.fifthScrollbar
                ), 0, 15, TimeUnit.MINUTES);
    }
}