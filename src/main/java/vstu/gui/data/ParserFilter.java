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
    public static List<Data> includeList = Collections.synchronizedList(new ArrayList<Data>());
    public static List<Data> exludeList = Collections.synchronizedList(new ArrayList<Data>());

    public static class Data {
        /**
         * Значение фильтра
         */
        private String data;
        /**
         * regexp?
         */
        private boolean regexp;

        public Data(String data){
            this.data = data;
        }
        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public boolean isRegexp() {
            return regexp;
        }

        public void setRegexp(boolean regexp) {
            this.regexp = regexp;
        }
    }
}