package vstu.gui.data;

import java.io.*;
import java.util.Properties;

/**
 * Created by Lopatin on 22.03.2014.
 */
public class OptionsProperties {
    /**
     * Получение всех ссылок на сайте
     */
    public static boolean urlSelectorEnabled = true;
    /**
     * Получение всех изображений
     */
    public static boolean imgSelectorEnabled = true;
    /**
     * Получение всех CSS
     */
    public static boolean cssSelectorEnabled = true;
    /**
     * Получение всех JS на сайте
     */
    public static boolean jsSelectorEnabled = true;
    /**
     * Таймаут ожидания запроса
     */
    public static Integer timeout = 1000;
    /**
     * Максимальный уровнь захода по ссылкам
     */
    public static Integer maxLvl = 100;
    /**
     * Исключение повторяющихся ссылок
     */
    public static boolean excludeRepeatedUrl;
    /**
     * Выход за пределы сайта
     */
    public static boolean movingBeyond;
    /**
     * Движение по поддоменам
     */
    public static boolean movingSubDomain;
    /**
     * Язык интерфейса программы
     */
    public static Language language = Language.ENGLISH;
    /**
     * Количество дополнительных поток (1 + это число), которые будут парсить страницу
     */
    public static int countThread;

    /**
     * Видимые колонки ввиде 0101010101111 - где 1-видно, 0-не видно
     */
    public static String visibleColumns = "1111111";


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
        jsSelectorEnabled = Boolean.parseBoolean(properties.getProperty("jsSelectorEnabled", "true"));
        excludeRepeatedUrl = Boolean.parseBoolean(properties.getProperty("excludeRepeatedUrl", "false"));
        movingSubDomain = Boolean.parseBoolean(properties.getProperty("movingSubDomain", "false"));
        movingBeyond = Boolean.parseBoolean(properties.getProperty("movingBeyond", "false"));
        maxLvl = Integer.parseInt(properties.getProperty("maxLvl", "100"));
        timeout = Integer.parseInt(properties.getProperty("timeout", "1000"));
        language = Language.valueOf(properties.getProperty("language", "ENGLISH"));
        countThread = Integer.parseInt(properties.getProperty("countThread", "20"));
        visibleColumns = properties.getProperty("visibleColumns","1111111");
    }

    public static void save() {
        File f = new File(FILE_NAME);
        Properties properties = new Properties();

        properties.setProperty("urlSelectorEnabled", Boolean.toString(urlSelectorEnabled));
        properties.setProperty("imgSelectorEnabled", Boolean.toString(imgSelectorEnabled));
        properties.setProperty("cssSelectorEnabled", Boolean.toString(cssSelectorEnabled));
        properties.setProperty("jsSelectorEnabled", Boolean.toString(jsSelectorEnabled));
        properties.setProperty("excludeRepeatedUrl", Boolean.toString(excludeRepeatedUrl));
        properties.setProperty("movingBeyond", Boolean.toString(movingBeyond));
        properties.setProperty("movingSubDomain", Boolean.toString(movingSubDomain));
        properties.setProperty("maxLvl", "100");
        properties.setProperty("timeout", Integer.toString(timeout));
        properties.setProperty("language", language.toString());
        properties.setProperty("countThread", Integer.toString(countThread));
        properties.setProperty("visibleColumns",visibleColumns);

        try {
            properties.store(new FileOutputStream(f), "vstu xenu file options");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
