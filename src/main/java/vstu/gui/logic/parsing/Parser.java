package vstu.gui.logic.parsing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vstu.gui.data.OptionsProperties;
import vstu.gui.data.ParserFilter;
import vstu.gui.forms.main.ITableWorker;

import java.net.IDN;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Lopatin on 20.03.14.
 */
public class Parser {

    /**
     * Список проверенных объектов
     */
    private List<String> checkedUrlList = Collections.synchronizedList(new ArrayList<String>());
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
    public List<OtherThread> threads = new ArrayList<>();
    /**
     * Главный поток
     */
    public MainThread mainThread;

    /**
     * Любая ссылка должна начинаться с этого url, чтобы не выходить за пределы сайта
     */
    private String mainUrl;

    public void startCheck(String url) {
        if (!url.contains("http://")) {
            url = "http://" + url;
        }

        mainUrl = getMainUrl(url);
        /**
         * Пять потоков занимаются чеканьем
         */
        for (int i = 0; i < OptionsProperties.countThread - 1; i++) {
            OtherThread thread;
            thread = new OtherThread();

            thread.start();
            threads.add(thread);
        }

        mainThread = new MainThread(url);

        mainThread.start();
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
            while (true && !stop) {

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
    public void checkUrl(String url, final int lvl) {
        // Мы пытаемся остановить?
        Thread thread = Thread.currentThread();
        if (thread instanceof OtherThread) {
            if (((OtherThread) thread).isStop()) {
                return;
            }
        } else if (thread instanceof MainThread) {
            if (((MainThread) thread).isStop()) {
                return;
            }
        }

        // Убиваем последний слеш
        if (url.length() > 0 && url.charAt(url.length() - 1) == '\\') {
            StringBuilder sb = new StringBuilder(url);
            sb.deleteCharAt(url.length() - 1);

            url = sb.toString();
        }

        // Ссылка на том же сайте? (Чтобы за пределы сайта не расползаться)
        if (!OptionsProperties.movingBeyond && !url.contains(mainUrl)) {
            return;
        }

        // Повторно не заходим
        if (OptionsProperties.excludeRepeatedUrl && checkedUrlList.contains(url)) {
            return;
        }

        // Глубина уровня
        if (OptionsProperties.maxLvl != -1 && lvl > OptionsProperties.maxLvl) {
            return;
        }

        // Добавляем строку в таблицу
        checkedUrlList.add(url);

        String url_punny = toPunnyCode(url);

        Connection.Response response;
        try {
            response = Jsoup.connect(url_punny).
                    ignoreContentType(true).
                    ignoreHttpErrors(true).
                    timeout(OptionsProperties.timeout)
                    .userAgent("Mozilla/17.0")
                    .followRedirects(true)
                    .execute();

            Integer code = response.statusCode();
            Integer bytes = response.bodyAsBytes().length;
            String type = response.contentType();
            String charset = response.charset();

            vstu.gui.forms.main.UrlData
                    data = new vstu.gui.forms.main.UrlData(url, code.toString(), lvl, type, charset, bytes, url);


            tableWorker.addRow(data);

            // Это html-страница
            if (response.contentType().contains("text/html")) {

                // Проверка на содержимое тега
                Elements elements = response.parse().getAllElements();
                for (String tag : ParserFilter.tagList) {
                    for (Element element : elements) {
                        if (element.tagName().equals(tag)) {
                            data.getContainsTag().add(tag);
                            break;
                        }
                    }
                }

                // Проверка на содержимое текста
                String html_data = response.body();
                for (String containsTest : ParserFilter.searchList) {
                    if (html_data.contains(containsTest)) {
                        data.getContainsText().add(containsTest);
                    }
                }

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
        List<String> ur = new LinkedList<>();

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
            Elements jsLink = doc.select("script[src]");
            for (Element element : jsLink) {
                ur.add(element.attr("abs:src"));
            }
        }

        // Фильруем список на ссылки исключённые из обработки
        for (String exclude : ParserFilter.exludeList) {
            Pattern pattern = Pattern.compile(exclude);

            Iterator<String> iterator = ur.iterator();
            // Ссылка есть в исключающем списке - удаляем
            while (iterator.hasNext()) {
                String current = iterator.next();
                if (pattern.matcher(current).find()) {
                    iterator.remove();
                }
            }
        }

        // Фильруем список на ссылки, который должны быть в обработки
        for (String include : ParserFilter.includeList) {
            Iterator<String> iterator = ur.iterator();

            Pattern pattern = Pattern.compile(include);
            // Ссылки нет во включающем списке - удалям
            while (iterator.hasNext()) {
                String current = iterator.next();
                if (!pattern.matcher(current).find()) {
                    iterator.remove();
                }
            }
        }

        return ur;
    }

    /**
     * Закончилась ли работа?
     *
     * @return
     */
    public boolean isFinish() {
        return queue.size() == 0;
    }

    private static String getMainUrl(String url) {
        int startIndexof = url.indexOf("//");
        if (startIndexof == -1) {
            return "";
        }

        int secondIndexOf = url.indexOf("/", startIndexof + 2);

        if (secondIndexOf == -1) {
            secondIndexOf = url.length();
        }

        return url.substring(startIndexof + 2, secondIndexOf);

    }

    private static String toPunnyCode(String url) {
        String result = url;
        // IDN.toASCI();  - только для русской части
        return result;
    }
}
