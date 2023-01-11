package net.safefleet.prod.productionscheduler.thread;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

import java.util.Set;


public class AutoScrollingThread implements Runnable {
    private ScrollPane[] scrollPanes;
    private boolean[] down;

    public AutoScrollingThread(ScrollPane... scrollPanes) {
        this.scrollPanes = scrollPanes;
        this.down = new boolean[]{true, true, true, true, true};
    }

    @Override
    public void run() {
        for(int i = 0; i < this.scrollPanes.length; i++) {
            Set<Node> nodes = this.scrollPanes[i].lookupAll(".scroll-bar");

            for(Node node : nodes) {
                if (node instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) node;
                    if(bar.isVisible()) {
                        if(this.down[i]) {
                            int finalI = i;
                            Platform.runLater(() -> {
                                bar.adjustValue(bar.getValue() + .001);
                                if(bar.getValue() >= 1) {
                                    this.down[finalI] = false;
                                }
                            });
                        } else {
                            int finalI1 = i;
                            Platform.runLater(() -> {
                                bar.adjustValue(bar.getValue() - .001);
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
