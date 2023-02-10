package net.safefleet.prod.productionscheduler.data.files;

import java.io.File;

public abstract class DataFile<T> {
    protected File file;


    public DataFile(String path) {
        this.file = new File(path);

        writeDefaults();
    }

    protected abstract void writeDefaults();
    public abstract T read();
}
