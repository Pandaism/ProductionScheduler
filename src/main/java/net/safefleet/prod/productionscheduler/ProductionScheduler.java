package net.safefleet.prod.productionscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductionScheduler extends Application {
    public static Properties properties;
    public static List<String> shippableItems;

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
        File settingsFile = new File("./settings.properties");
        File shippableItemsFile = new File("./shippable_items.txt");
        shippableItems = new ArrayList<>();

        if(!shippableItemsFile.exists()) {
            try {
                shippableItemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(shippableItemsFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(shippableItemsFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    shippableItems.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            properties = new Properties();

            if (!settingsFile.exists()) {
                OutputStream outputStream = new FileOutputStream(settingsFile);
                properties.setProperty("db.url", "localhost");
                properties.setProperty("db.database", "database");
                properties.setProperty("db.user", "user");
                properties.setProperty("db.password", "password");

                properties.store(outputStream, null);
                outputStream.close();
            } else {
                InputStream inputStream = new FileInputStream(settingsFile);

                properties.load(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }


}