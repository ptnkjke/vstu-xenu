package vstu.gui.logic.parsing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vstu.gui.data.OptionsProperties;
import vstu.gui.data.ParserFilter;
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
    private Queue<vstu.gui.logic.parsing.UrlData> queue = new LinkedList<vstu.gui.logic.parsing.UrlData>();
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
    public List<Thread> threads = new ArrayList<>();
    /**
     * Главный поток
     */
    public MainThread mainThread;

    public void startCheck(final String url) {
        /**
         * Пять потоков занимаются чеканьем
         */
        for (int i = 0; i < 10; i++) {
            Thread thread;
            thread = new OtherThread();

            thread.start();
            threads.add(thread);
        }

        mainThread = new MainThread(url);

        mainThread.start();
        threads.add(mainThread);
    }

    /**
     * Класс основного потока
     */
    private class MainThread extends Thread {
        private boolean stop = false;

        private String url;

        public MainThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            checkUrl(url, 0);
            stop = true;
        }

        @Override
        public void interrupt() {
            stop = true;
            super.interrupt();
        }

        public boolean isStop() {
            return stop;
        }
    }

    /**
     * Класс побочных потоков
     */
    private class OtherThread extends Thread {
        private boolean stop = false;


        @Override
        public void run() {
            while (true) {

                while (!queue.isEmpty()) {
                    vstu.gui.logic.parsing.UrlData ud = null;

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

        @Override
        public void interrupt() {
            stop = true;
            super.interrupt();
        }

        public boolean isStop() {
            return stop;
        }
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

            tableWorker.addRow(new vstu.gui.forms.main.UrlData(url, code.toString(), lvl, type, charset, bytes, url));

            // Это html-страница
            if (response.contentType().contains("text/html")) {

                List<String> urls = getAbsUrls(response.parse());
                synchronized (sync) {
                    for (String _url : urls) {
                        queue.add(new vstu.gui.logic.parsing.UrlData(_url, lvl + 1));
                    }
                }
                tableWorker.updateCountUrlInQueue(queue.size());
            }

        } catch (java.net.SocketTimeoutException exception) {
            tableWorker.addRow(new vstu.gui.forms.main.UrlData(url, "timeout", lvl, "", "", 0, url));
            exception.printStackTrace();
        } catch (UnknownHostException e) {
            tableWorker.addRow(new vstu.gui.forms.main.UrlData(url, "unknown host", lvl, "", "", 0, url));
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

    /**
     * Закончилась ли работа?
     *
     * @return
     */
    public boolean isFinish() {
        // TODO: Нужно как-то это сделать

        for (Thread thread : threads) {
            if (thread.isAlive()) {
                return false;
            }
        }

        return true;
    }
}
