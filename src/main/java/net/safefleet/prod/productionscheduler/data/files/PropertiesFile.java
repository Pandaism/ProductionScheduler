package net.safefleet.prod.productionscheduler.data.files;

import java.io.*;
import java.util.Properties;

public class PropertiesFile extends DataFile<Properties> {
    private Properties properties;

    public PropertiesFile() {
        super("./settings.properties");

        this.properties = new Properties();
    }

    @Override
    protected void writeDefaults() {
        if(!super.file.exists()) {
            OutputStream outputStream;
            try {
                outputStream = new FileOutputStream(super.file);
                this.properties.setProperty("db.url", "localhost");
                this.properties.setProperty("db.database", "database");
                this.properties.setProperty("db.user", "user");
                this.properties.setProperty("db.password", "password");

                this.properties.store(outputStream, null);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Properties read() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(super.file);
            this.properties.load(inputStream);
            inputStream.close();

            return this.properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
