package net.safefleet.prod.productionscheduler.data.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShippableItemFile extends DataFile<List<String>> {

    public ShippableItemFile() {
        super("./shippable_items.txt");
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
