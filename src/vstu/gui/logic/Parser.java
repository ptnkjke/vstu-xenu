package vstu.gui.logic;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vstu.gui.forms.MainForm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lopatin on 20.03.14.
 */
public class Parser {

    /**
     * Список проверенных Url
     */
    private static List<String> checkedUrlList = new ArrayList<>();
    /**
     * Очередь ссылок на проверки
     */
    private static LinkedBlockingQueue<UrlData> queue = new LinkedBlockingQueue<>(5000);
    /**
     * Максимальный уровень рекурсии
     */
    private static final int MAX_LEVEL = 3;
    /**
     * Максимальны йтаймаут для проверки ссылки ms
     */
    private static final int MAX_TIMEOUT = 500;

    private static Object sync = new Object();


    public static void startCheck(final String url) {

        /**
         * Пять потоков занимаются чеканьем
         */
        for (int i = 0; i < 5; i++) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        while (!queue.isEmpty()) {
                            UrlData ud = null;

                            synchronized (sync) {
                                try {
                                    ud = queue.poll(1, TimeUnit.SECONDS);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                checkUrl(ud.getUrl(), ud.getLvl());
                            }
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

        Thread thread1 = new Thread(new Runnable() {
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

            MainForm.getInstance().getTableModel().addRow(
                    new Object[]{
                            url, code, type, bytes, charset, lvl
                    }
            );

            // Это html-страница
            if (response.contentType().contains("text/html")) {
                List<String> urls = getAbsUrls(response.parse());
                for (String _url : urls) {
                    System.out.println("enter");


                    synchronized (sync) {
                        queue.put(new UrlData(_url, lvl + 1));
                    }
                    System.out.println("exit");
                }
            }

        } catch (java.net.SocketTimeoutException exception) {
        } catch (Exception e) {
            MainForm.getInstance().getTableModel().addRow(
                    new Object[]{
                            url, "timeout", "", "", "", lvl
                    }
            );
        }

    }


    public static List<String> getAbsUrls(Document doc) {
        List<String> ur = new ArrayList<>();

        Elements links = doc.select("a[href]");

        for (Element element : links) {
            ur.add(element.attr("abs:href"));
        }

        return ur;
    }

}
