package vstu.gui.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lopatin on 22.03.2014.
 */
public class ParserFilter {
    public static List<String> searchList = Collections.synchronizedList(new ArrayList<String>());
    public static List<String> tagList = Collections.synchronizedList(new ArrayList<String>());
    public static List<String> includeList = Collections.synchronizedList(new ArrayList<String>());
    public static List<String> exludeList = Collections.synchronizedList(new ArrayList<String>());
}
