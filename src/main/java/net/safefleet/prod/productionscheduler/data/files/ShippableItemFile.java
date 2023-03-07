package net.safefleet.prod.productionscheduler.data.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShippableItemFile extends DataFile<List<String>> {
    public enum SOURCE {
        COBAN("./coban_shippable_items.txt"), FLEETMIND("./fleetmind_shippable_items.txt"), SEON("./seon_shippable_items.txt");

        private String source;
        SOURCE(String source) {
            this.source = source;
        }

        public String getSource() {
            return source;
        }
    }

    public ShippableItemFile(SOURCE source) {
        super(source.getSource());
    }

    @Override
    public List<String> read() {
        List<String> shippableItems = new ArrayList<>();

        if(super.file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(super.file));
                String line;
                while ((line = reader.readLine()) != null) {
                    shippableItems.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return shippableItems;
    }


    @Override
    protected void writeDefaults() {
        if(!super.file.exists()) {
            try {
                super.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
