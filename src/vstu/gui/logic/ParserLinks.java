package vstu.gui.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucifer on 21.03.14.
 */
public class ParserLinks {


    public static List<String> checkedUrlList = new ArrayList<String>();
    /**
     * Максимальный уровень рекурсии
     */
    private static final int MAX_LEVEL = 3;
    /**
     * Максимальны йтаймаут для проверки ссылки ms
     */
    private static final int MAX_TIMEOUT = 1000;

    private static Object sync = new Object();

    public static void checkUrl(final String url, final int lvl) {



    }
}
