package vstu.gui.logic;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vstu.gui.data.Options;
import vstu.gui.forms.main.DataTable;
import vstu.gui.forms.main.ITableWorker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Lopatin on 20.03.14.
 */
public class Parser {

    /**
     * Список проверенных Url
     */
    public static List<String> checkedUrlList = new ArrayList<String>();
    static Thread thread1;

    /**
     * Максимальный уровень рекурсии
     */
    private static final int MAX_LEVEL = 3;
    /**
     * Максимальны йтаймаут для проверки ссылки ms
     */
    private static final int MAX_TIMEOUT = 5000;
    /**
     * Очередь ссылок на проверки
     */
    private static Queue<UrlData> queue = new LinkedList<UrlData>();

    private static Object sync = new Object();

    public static ITableWorker tableWorker;

    public static void startCheck(final String url) {
        /**
         * Пять потоков занимаются чеканьем
         */
        for (int i = 0; i < 10; i++) {
            Thread thread;
            thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {

                        while (!queue.isEmpty()) {
                            UrlData ud = null;

                            synchronized (sync) {
                                ud = queue.poll();
                            }

                            checkUrl(ud.getUrl(), ud.getLvl());
                        }

                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            thread.start();
        }

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                checkUrl(url, 0);
            }
        });

        thread1.start();


    }

    /**
     * Проверка url
     *
     * @param url Необходимы url
     * @param lvl Текущая глубина захода
     */
    public static void checkUrl(final String url, final int lvl) {


        Connection.Response response;

        // Повторно не заходим
        if (checkedUrlList.contains(url)) {
            return;
        }

        if (lvl > MAX_LEVEL) {
            return;
        }

        try {
            response = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).timeout(MAX_TIMEOUT).execute();

            // Добавляем строку в таблицу
            checkedUrlList.add(url);


            Integer code = response.statusCode();
            Integer bytes = response.bodyAsBytes().length;
            String type = response.contentType();
            String charset = response.charset();

            tableWorker.addRow(new DataTable(url, code.toString(), lvl, type, charset));


            // Это html-страница
            if (response.contentType().contains("text/html")) {

                List<String> urls = getAbsUrls(response.parse());
                synchronized (sync) {
                    for (String _url : urls) {
                        queue.add(new UrlData(_url, lvl + 1));
                    }
                }
                tableWorker.updateCountUrlInQueue(queue.size());
            }

        } catch (java.net.SocketTimeoutException exception) {
            tableWorker.addRow(new DataTable(url, "timeout", lvl, "", ""));
            exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static List<String> getAbsUrls(Document doc) {
        List<String> ur = new ArrayList<String>();

        if (Options.urlSelectorEnabled) {
            Elements links = doc.select("a[href]");
            for (Element element : links) {
                ur.add(element.attr("abs:href"));
            }
        }

        if (Options.imgSelectorEnabled) {
            Elements imglink = doc.select("img[src]");
            for (Element element : imglink) {
                ur.add(element.attr("abs:src"));
            }
        }

        if (Options.cssSelectorEnabled) {
            Elements csslink = doc.select("link[href]");
            for (Element element : csslink) {
                ur.add(element.attr("abs:href"));
            }
        }


        return ur;
    }

    public static void stop() {
        thread1.stop();

    }

}
