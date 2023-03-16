package net.safefleet.prod.productionscheduler.data.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * PropertiesFile is a class representing a properties file.
 * It extends the DataFile class with the type parameter set to Properties.
 */
public class PropertiesFile extends DataFile<Properties> {
    // Declare a logger for debugging and logging purposes
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFile.class);
    // Declare a Properties object
    private final Properties properties;

    /**
     * Constructor for the PropertiesFile class.
     */
    public PropertiesFile() {
        // Call the superclass constructor with the properties file path
        super("./settings.properties");

        // Initialize the properties object
        this.properties = new Properties();
    }

    /**
     * Override the writeDefaults method from the superclass.
     * Writes default values to the properties file if it doesn't exist.
     */
    @Override
    protected void writeDefaults() {
        // If the properties file doesn't exist, create and populate it with default values
        if(!super.file.exists()) {
            OutputStream outputStream;
            try {
                // Create a new FileOutputStream for the properties file
                outputStream = new FileOutputStream(super.file);

                // Set default properties values
                // Add default values for each company's database connection properties
                this.properties.setProperty("coban.db.url", "localhost");
                this.properties.setProperty("coban.db.database", "database");
                this.properties.setProperty("coban.db.user", "user");
                this.properties.setProperty("coban.db.password", "password");
                this.properties.setProperty("fleetmind.db.url", "localhost");
                this.properties.setProperty("fleetmind.db.database", "database");
                this.properties.setProperty("fleetmind.db.user", "user");
                this.properties.setProperty("fleetmind.db.password", "password");
                this.properties.setProperty("seon.db.url", "localhost");
                this.properties.setProperty("seon.db.database", "database");
                this.properties.setProperty("seon.db.user", "user");
                this.properties.setProperty("seon.db.password", "password");

                // Store the properties in the output stream and close it
                this.properties.store(outputStream, null);
                outputStream.close();
            } catch (IOException e) {
                // Log any errors that occur while writing the default values
                LOGGER.error("Error writing defaults " + e);
                LOGGER.trace("{}", e.getMessage());
            }
        }
    }

    /**
     * Override the read method from the superclass.
     * Reads data from the properties file.
     *
     * @return The loaded Properties object, or null if an error occurs.
     */
    @Override
    public Properties read() {
        InputStream inputStream;
        try {
            // Create a new FileInputStream for the properties file
            inputStream = new FileInputStream(super.file);

            // Load the properties from the input stream
            this.properties.load(inputStream);

            // Close the input stream
            inputStream.close();

            // Log the loaded properties
            LOGGER.info("Properties loaded:\n {}", this.properties);

            // Return the loaded properties
            return this.properties;
        } catch (IOException e) {
            // Log any errors that occur while reading the properties file
            LOGGER.error("Error reading properties file: " + e);
            LOGGER.trace("{}", e.getMessage());
        }
        // If an error occurs, return null
        return null;
    }
}
