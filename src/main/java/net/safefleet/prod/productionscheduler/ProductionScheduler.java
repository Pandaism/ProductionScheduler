package net.safefleet.prod.productionscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.safefleet.prod.productionscheduler.controllers.MainFrameController;
import net.safefleet.prod.productionscheduler.data.files.PropertiesFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * ProductionScheduler class is the main class for the Production Scheduler application.
 * It extends the JavaFX Application class to create and manage the application's UI.
 */
public class ProductionScheduler extends Application {
    // Create a Logger instance to log messages
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductionScheduler.class);

    // Declare a Properties object to hold application properties
    public static Properties properties;

    /**
     * The start method is the entry point for the JavaFX application.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If there is an issue loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Load the main-view FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(ProductionScheduler.class.getResource("main-view.fxml"));

            // Create a new Scene with the loaded FXML layout and set dimensions
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

            // Create an access point into the MainFrameController class
            MainFrameController controller = fxmlLoader.getController();

            // Add the dark-theme CSS stylesheet to the scene
            scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());

            // Set the stage to full screen
            stage.setFullScreen(true);

            // Set the scene onto the stage
            stage.setScene(scene);

            // Set the stage to call shutdown method to safely shutdown threads running in the background
            stage.setOnCloseRequest(event -> controller.shutdown());

            // Show the stage
            stage.show();

            // Log a message indicating the GUI has been initialized
            LOGGER.info("GUI initialized");
        } catch (IOException e) {
            LOGGER.error("An error occurred while loading the FXML file", e);
        }
    }

    /**
     * The main method is the starting point of the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        // Read the properties file
        properties = new PropertiesFile().read();

        // Check if the properties object is not null
        if (properties != null) {
            // Launch the JavaFX application
            launch(args);
        } else {
            // Log an error message if the properties file returned null
            LOGGER.error("Properties file returned null");
        }
    }
}