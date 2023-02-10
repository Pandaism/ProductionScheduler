package net.safefleet.prod.productionscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.safefleet.prod.productionscheduler.data.files.PropertiesFile;

import java.io.*;
import java.util.Properties;

public class ProductionScheduler extends Application {
    public static Properties properties;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionScheduler.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        properties = new PropertiesFile().read();

        if(properties != null) {
            launch(args);
        } else {
            System.out.println("Properties file return null");
        }
    }


}