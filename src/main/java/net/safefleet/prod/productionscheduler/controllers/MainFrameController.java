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

public class MainFrameController {
    public Label headerText;

    public ScrollPane firstScrollbar;
    public ScrollPane secondScrollbar;
    public ScrollPane thirdScrollbar;
    public ScrollPane fourthScrollbar;
    public ScrollPane fifthScrollbar;
    public HBox centerBox;

    public MainFrameController() {
        this.firstScrollbar = new ScrollPane();
        this.secondScrollbar = new ScrollPane();
        this.thirdScrollbar = new ScrollPane();
        this.fourthScrollbar = new ScrollPane();
        this.fifthScrollbar = new ScrollPane();
    }

    public void initialize() {
        this.firstScrollbar.setFitToHeight(true);
        this.firstScrollbar.setFitToWidth(true);
        HBox.setHgrow(this.firstScrollbar, Priority.ALWAYS);

        this.secondScrollbar.setFitToHeight(true);
        this.secondScrollbar.setFitToWidth(true);
        HBox.setHgrow(this.secondScrollbar, Priority.ALWAYS);

        this.thirdScrollbar.setFitToHeight(true);
        this.thirdScrollbar.setFitToWidth(true);
        HBox.setHgrow(this.thirdScrollbar, Priority.ALWAYS);

        this.fourthScrollbar.setFitToHeight(true);
        this.fourthScrollbar.setFitToWidth(true);
        HBox.setHgrow(this.fourthScrollbar, Priority.ALWAYS);

        this.fifthScrollbar.setFitToHeight(true);
        this.fifthScrollbar.setFitToWidth(true);
        HBox.setHgrow(this.fifthScrollbar, Priority.ALWAYS);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(new DateUpdaterThread(this.headerText), 0, 1, TimeUnit.DAYS);
        executorService.scheduleAtFixedRate(new AutoScrollingThread(this.firstScrollbar, this.secondScrollbar, this.thirdScrollbar, this.fourthScrollbar, this.fifthScrollbar), 0, 5, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(
                new ExpandableReaderThread(
                        this.centerBox, this.firstScrollbar, this.secondScrollbar, this.thirdScrollbar, this.fourthScrollbar, this.fifthScrollbar
                ), 0, 15, TimeUnit.MINUTES);


    }
}