package vstu.gui.data;

import java.io.*;
import java.util.Properties;

/**
 * Created by Lopatin on 22.03.2014.
 */
public class Options {
    public static boolean urlSelectorEnabled;
    public static boolean imgSelectorEnabled;
    public static boolean cssSelectorEnabled;
    public static Integer timeout;

    private static final String FILE_NAME = "options.ini";

    public static void load() {
        File f = new File(FILE_NAME);

        if (!f.exists()) {
            save();
        }

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlSelectorEnabled = Boolean.parseBoolean(properties.getProperty("urlSelectorEnabled", "true"));
        imgSelectorEnabled = Boolean.parseBoolean(properties.getProperty("imgSelectorEnabled", "true"));
        cssSelectorEnabled = Boolean.parseBoolean(properties.getProperty("cssSelectorEnabled", "true"));
        timeout = Integer.parseInt(properties.getProperty("timeout", "1000"));
    }

    public static void save() {
        File f = new File(FILE_NAME);
        Properties properties = new Properties();

        properties.setProperty("urlSelectorEnabled", Boolean.toString(urlSelectorEnabled));
        properties.setProperty("imgSelectorEnabled", Boolean.toString(imgSelectorEnabled));
        properties.setProperty("cssSelectorEnabled", Boolean.toString(cssSelectorEnabled));
        properties.setProperty("timeout", "1000");

        try {
            properties.store(new FileOutputStream(f), "vstu xenu file options");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
