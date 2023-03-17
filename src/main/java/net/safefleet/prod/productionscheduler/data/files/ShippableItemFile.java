package net.safefleet.prod.productionscheduler.data.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ShippableItemFile is a class representing a file containing shippable items.
 * It extends the DataFile class with the type parameter set to List<String>.
 */
public class ShippableItemFile extends DataFile<List<String>> {
    // Declare a logger for debugging and logging purposes
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippableItemFile.class);

    // Enum to represent the source of shippable item files
    public enum SOURCE {
        COBAN("./coban_shippable_items.txt"),
        FLEETMIND("./fleetmind_shippable_items.txt"),
        SEON("./seon_shippable_items.txt");

        private final String source;

        SOURCE(String source) {
            this.source = source;
        }

        public String getSource() {
            return source;
        }
    }

    private final SOURCE source;

    /**
     * Constructor for the ShippableItemFile class.
     * @param source The source of the shippable item file.
     */
    public ShippableItemFile(SOURCE source) {
        // Call the superclass constructor with the source file path
        super(source.getSource());
        this.source = source;
    }

    /**
     * Override the read method from the superclass.
     * Reads data from the shippable item file.
     *
     * @return The list of shippable items read from the file, or an empty list if the file doesn't exist.
     */
    @Override
    public List<String> read() {
        // Initialize a new list to store the shippable items
        List<String> shippableItems = new ArrayList<>();

        // If the shippable items file exists, read its contents
        if (super.file.exists()) {
            // Create a BufferedReader to read the file
            try(BufferedReader reader = new BufferedReader(new FileReader(super.file))) {
                String line;
                // Read the file line by line, adding each line to the shippableItems list
                while ((line = reader.readLine()) != null) {
                    shippableItems.add(line);
                }
            } catch (IOException e) {
                // Log any errors that occur while reading the shippable items file
                LOGGER.error("Error reading shipping file: {} " + e, this.source);
                LOGGER.trace("{}", e.getMessage());
            }
        }

        // Return the list of shippable items
        return shippableItems;
    }

    /**
     * Override the writeDefaults method from the superclass.
     * Creates an empty shippable items file if it doesn't exist.
     */
    @Override
    protected void writeDefaults() {
        // If the shippable items file doesn't exist, create it
        if (!super.file.exists()) {
            try {
                // Create a new file
                if(super.file.createNewFile()) {
                    LOGGER.info("File created: {}", super.file.getPath());
                }
            } catch (IOException e) {
                // Log any errors that occur while creating the shippable items file
                LOGGER.error("Error writing defaults: {} " + e, this.source);
                LOGGER.trace("{}", e.getMessage());
            }
        }
    }
}
