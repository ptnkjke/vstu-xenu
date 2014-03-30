package vstu.gui.logic.parsing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vstu.gui.data.OptionsProperties;
import vstu.gui.data.ParserFilter;
import vstu.gui.forms.main.DataTable;
import vstu.gui.forms.main.ITableWorker;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Lopatin on 20.03.14.
 */
public class Parser {

    /**
     * Список проверенных объектов
     */
    private List<String> checkedUrlList = new ArrayList<String>();
    /**
     * Очередь ссылок на проверки
     */
    private Queue<UrlData> queue = new LinkedList<UrlData>();
    /**
     * Объект для синхронизации потоков
     */
    private Object sync = new Object();
    /**
     * Родитель
     */
    public ITableWorker tableWorker;
    /**
     * Список всех созданных потоков
     */
    public List<Thread> list = new ArrayList<>();

    public void startCheck(final String url) {
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
            list.add(thread);
        }

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                checkUrl(url, 0);
            }
        });

        thread1.start();
        list.add(thread1);


    }

    /**
     * Проверка url
     *
     * @param url Необходимы url
     * @param lvl Текущая глубина захода
     */
    public void checkUrl(final String url, final int lvl) {


        Connection.Response response;

        // Повторно не заходим
        if (OptionsProperties.excludeRepeatedUrl && checkedUrlList.contains(url)) {
            return;
        }

        // Глубина уровня
        if (OptionsProperties.maxLvl != -1 && lvl > OptionsProperties.maxLvl) {
            return;
        }

        try {
            response = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).timeout(OptionsProperties.timeout).execute();

            // Добавляем строку в таблицу
            checkedUrlList.add(url);

            Integer code = response.statusCode();
            Integer bytes = response.bodyAsBytes().length;
            String type = response.contentType();
            String charset = response.charset();

            tableWorker.addRow(new DataTable(url, code.toString(), lvl, type, charset, bytes));

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
            tableWorker.addRow(new DataTable(url, "timeout", lvl, "", "", 0));
            exception.printStackTrace();
        } catch (UnknownHostException e) {
            tableWorker.addRow(new DataTable(url, "unknown host", lvl, "", "", 0));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<String> getAbsUrls(Document doc) {
        List<String> ur = new ArrayList<String>();

        // url - селектор
        if (OptionsProperties.urlSelectorEnabled) {
            Elements links = doc.select("a[href]");
            for (Element element : links) {
                ur.add(element.attr("abs:href"));
            }
        }

        // img - селектор
        if (OptionsProperties.imgSelectorEnabled) {
            Elements imglink = doc.select("img[src]");
            for (Element element : imglink) {
                ur.add(element.attr("abs:src"));
            }
        }

        // css - селектор
        if (OptionsProperties.cssSelectorEnabled) {
            Elements csslink = doc.select("link[href]");
            for (Element element : csslink) {
                ur.add(element.attr("abs:href"));
            }
        }

        // js - селектор
        if (OptionsProperties.jsSelectorEnabled) {

        }

        // Фильруем список на ссылки исключённые из обработки
        if (ParserFilter.exludeList.size() > 0) {
            for (String url : ParserFilter.exludeList) {

            }
        }

        // Фильруем список на ссылки, который должны быть в обработки
        if (ParserFilter.includeList.size() > 0) {

        }

        return ur;
    }
}
