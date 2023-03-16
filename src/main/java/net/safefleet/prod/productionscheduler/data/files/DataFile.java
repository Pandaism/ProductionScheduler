package net.safefleet.prod.productionscheduler.data.files;

import java.io.File;

/**
 * DataFile is an abstract class representing a generic data file.
 *
 * @param <T> The type of data to be read from the file.
 */
public abstract class DataFile<T> {
    // Declare a File object
    protected File file;

    /**
     * Constructor that takes a file path as a parameter.
     *
     * @param path A String representing the file path.
     */
    public DataFile(String path) {
        // Initialize the file with the provided path
        this.file = new File(path);

        // Write default values to the file
        writeDefaults();
    }

    /**
     * Abstract method to write default values to the file.
     */
    protected abstract void writeDefaults();

    /**
     * Abstract method to read data from the file.
     *
     * @return An object of type T containing the data read from the file.
     */
    public abstract T read();
}
