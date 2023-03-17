package net.safefleet.prod.productionscheduler.thread;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

import java.util.Arrays;
import java.util.Set;

/**
 * AutoScrollingThread is a custom Runnable class for implementing automatic
 * scrolling of multiple ScrollPane objects. This class provides a mechanism to
 * scroll up and down through the ScrollPanes in a continuous loop.
 */
public class AutoScrollingThread implements Runnable {
    private double scrollSteps;
    private final ScrollPane[] scrollPanes;
    private final boolean[] down;

    /**
     * Constructor taking a variable number of ScrollPane objects.
     *
     * @param scrollPanes The ScrollPane objects to be auto-scrolled.
     */
    public AutoScrollingThread(double scrollSteps, ScrollPane... scrollPanes) {
        this.scrollSteps = scrollSteps;
        this.scrollPanes = scrollPanes;

        // Fill 'down' array (size of argument passed) with true
        this.down = new boolean[scrollPanes.length];
        Arrays.fill(this.down, true);
    }

    /**
     * The run method of the Runnable interface, which will be executed when the
     * thread is started. This method implements the auto-scrolling logic for the
     * provided ScrollPane objects.
     */
    @Override
    public void run() {
        // Iterate through all the provided ScrollPane objects
        for(int i = 0; i < this.scrollPanes.length; i++) {
            // Get all scroll-bar nodes in the current ScrollPane
            Set<Node> nodes = this.scrollPanes[i].lookupAll(".scroll-bar");

            // Iterate through the retrieved nodes
            for(Node node : nodes) {
                // Check if the node is an instance of ScrollBar
                if (node instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) node;
                    // If the ScrollBar is visible, perform the auto-scrolling
                    if(bar.isVisible()) {
                        // If the 'down' flag is true for the current ScrollPane, scroll down
                        if(this.down[i]) {
                            int finalI = i;
                            // Update the ScrollBar value on the JavaFX application thread
                            Platform.runLater(() -> {
                                bar.adjustValue(bar.getValue() + this.scrollSteps);
                                // If the ScrollBar reaches the bottom, set the 'down' flag to false
                                if(bar.getValue() >= 1) {
                                    this.down[finalI] = false;
                                }
                            });
                        } else {
                            // If the 'down' flag is false, scroll up
                            int finalI1 = i;
                            // Update the ScrollBar value on the JavaFX application thread
                            Platform.runLater(() -> {
                                bar.adjustValue(bar.getValue() - this.scrollSteps);
                                // If the ScrollBar reaches the top, set the 'down' flag to true
                                if(bar.getValue() <= 0) {
                                    this.down[finalI1] = true;
                                }
                            });

                        }
                    }
                }
            }
        }
    }
}
