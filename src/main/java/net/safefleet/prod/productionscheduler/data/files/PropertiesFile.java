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
