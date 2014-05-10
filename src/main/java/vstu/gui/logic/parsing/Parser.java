package vstu.gui.logic.parsing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vstu.gui.data.OptionsProperties;
import vstu.gui.data.ParserFilter;
import vstu.gui.forms.main.ITableWorker;
import vstu.gui.forms.main.UrlData;

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
    private Queue<UrlDataQ> queue = new LinkedList<UrlDataQ>();
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
            checkUrl(url, 0, null);
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
            while (!stop) {

                while (!queue.isEmpty()) {
                    UrlDataQ ud = null;

                    synchronized (sync) {
                        ud = queue.poll();
                    }
                    checkUrl(ud.getUrl(), ud.getLvl(), ud.getParent());
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
     * @param url    Необходимы url
     * @param lvl    Текущая глубина захода
     * @param parent Родительский элемент
     */
    public void checkUrl(String url, final int lvl, UrlData parent) {
        // Мы пытаемся остановить?
        if (isMustStop()) {
            return;
        }

        // Убиваем последний слеш
        if (url.length() > 0 && url.charAt(url.length() - 1) == '\\') {
            StringBuilder sb = new StringBuilder(url);
            sb.deleteCharAt(url.length() - 1);

            url = sb.toString();
        }

        // Имеет ли ссылка якорь? Чтобы ссылки http://news.yandex.ru/?lang=ru и http://news.yandex.ru/?lang=ru#abcde не были одинаковыми
        if (url.contains("#")) {
            url = url.split("#")[0];
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

        // Домен
        String domain = getDomain(url);

        if (OptionsProperties.movingSubDomain) {
            // TODO: Поддомен?
        }


        // Добавляем строку в таблицу
        checkedUrlList.add(url);

        // Домен заменённый на punny
        String punny = getPunnyPart(url);

        String url_punny = toPunnyCode(url);

        Connection.Response response;
        try {
            long time1 = System.nanoTime();
            response = Jsoup.connect(url_punny).
                    ignoreContentType(true).
                    ignoreHttpErrors(true).
                    timeout(OptionsProperties.timeout)
                    .userAgent("Mozilla/17.0")
                    .followRedirects(true)
                    .execute();

            long time2 = System.nanoTime();

            Integer code = response.statusCode();
            Integer bytes = response.bodyAsBytes().length;
            String type = response.contentType();
            String charset = response.charset();

            UrlData data = new UrlData(url, code.toString(), lvl, type, charset, bytes, url, (time2 - time1) / 1000000);

            // Это html-страница
            if (response.contentType().contains("text/html")) {

                // Проверка на содержимое тега
                Elements elements = response.parse().getAllElements();
                for (String tag : ParserFilter.tagList) {
                    StringBuilder sb = new StringBuilder();
                    for (Element element : elements) {
                        if (element.tagName().equals(tag)) {
                            sb.append(element.text()).append("||");
                        }
                    }
                    // Пытаемся остановиться?
                    if (isMustStop()) {
                        return;
                    }
                    data.getContainsTag().put(tag, sb.toString());
                }

                // Проверка на содержимое текста (подсчитываем количество)
                String html_data = response.body();
                for (String containsTest : ParserFilter.searchList) {
                    int count = getCountIndexOf(html_data, containsTest);
                    data.getContainsText().put(containsTest, count);

                    // Пытаемся остановиться?
                    if (isMustStop()) {
                        return;
                    }
                }

                List<String> urls = getAbsUrls(response.parse(), punny, domain, lvl);
                synchronized (sync) {
                    for (String _url : urls) {
                        queue.add(new UrlDataQ(_url, lvl + 1, data));
                    }
                }
                if(!isMustStop()) {
                    tableWorker.updateCountUrlInQueue(queue.size());
                }
            }
            if(!isMustStop()) {
                tableWorker.addRow(data);
            }

            // Добавляем в родителя ребёнка
            if (parent != null) {
                parent.addChildren(data);
            }

        } catch (java.net.SocketTimeoutException exception) {
            if(!isMustStop()) {
                tableWorker.addRow(new vstu.gui.forms.main.UrlData(url, "timeout", lvl, "", "", 0, url, OptionsProperties.timeout));
            }
        } catch (UnknownHostException e) {
            if(!isMustStop()) {
                tableWorker.addRow(new vstu.gui.forms.main.UrlData(url, "unknown host", lvl, "", "", 0, url, -1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(url_punny);
        }

    }

    public boolean isMustStop() {
        Thread thread = Thread.currentThread();
        if (thread instanceof OtherThread) {
            if (((OtherThread) thread).isStop()) {
                return true;
            }
        } else if (thread instanceof MainThread) {
            if (((MainThread) thread).isStop()) {
                return true;
            }
        }

        return false;
    }

    public List<String> getAbsUrls(Document doc, String punny, String originalDomain, int lvl) {
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

        // На первой странице игнорируем исключающие ссылки
        if (lvl != 0) {
            // Фильруем список на ссылки исключённые из обработки
            for (ParserFilter.Data exclude : ParserFilter.exludeList) {
                Pattern pattern = Pattern.compile(exclude.getData());

                Iterator<String> iterator = ur.iterator();
                // Ссылка есть в исключающем списке - удаляем
                while (iterator.hasNext()) {
                    String current = iterator.next();
                    if (!exclude.isRegexp()) {
                        if (pattern.matcher(current).find()) {
                            iterator.remove();
                        }
                    } else {
                        if (pattern.matcher(current).matches()) {
                            iterator.remove();
                        }
                    }
                }
            }
        }


        // На первой странице игнорируем исключающие ссылки
        if (lvl != 0) {
            // Фильруем список на ссылки, который должны быть в обработки
            for (ParserFilter.Data include : ParserFilter.includeList) {
                Iterator<String> iterator = ur.iterator();

                Pattern pattern = Pattern.compile(include.getData());
                // Ссылки нет во включающем списке - удалям
                while (iterator.hasNext()) {
                    String current = iterator.next();
                    if (!include.isRegexp()) {
                        if (!pattern.matcher(current).find()) {
                            iterator.remove();
                        }
                    } else {
                        if (!pattern.matcher(current).matches()) {
                            iterator.remove();
                        }
                    }
                }
            }
        }


        // Убираем punnycode из строк и меням на нормальные Url
        List<String> replaces = new ArrayList<>();

        for (String str : ur) {
            String new_str = str.replace(punny, originalDomain);
            if (!new_str.contains("http://")) {
                new_str = "http://" + new_str;
            }
            replaces.add(new_str);
        }

        return replaces;
    }

    /**
     * Закончилась ли работа?
     *
     * @return
     */
    public boolean isFinish() {
        return queue.size() == 0;
    }

    /**
     * Получение стартового домена
     *
     * @param url
     * @return
     */
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
        // Только домен нужно конвертировать
        String domain = getDomain(url);
        String punny = null;
        try {
            punny = IDN.toASCII(domain);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(domain);
        }
        if (punny != null) {
            return url.replace(domain, punny);
        } else {
            return domain;
        }
    }


    private static String getPunnyPart(String url) {
        // Только домен нужно конвертировать
        String domain = getDomain(url);
        String punny = null;
        try {
            punny = IDN.toASCII(domain);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(domain);
        }
        return punny;

    }

    private static String getDomain(String url) {
        int le = "http://".length();
        int start = url.indexOf("http://");
        int end = url.indexOf("/", le);
        if (end == -1) {
            return url.substring(start + le);
        } else {
            return url.substring(start + le, end);
        }
    }

    private static int getCountIndexOf(String inner, String founder) {
        int count = 0;
        int indexNext = 0;
        while (indexNext != -1) {
            int indexOf = inner.indexOf(founder, indexNext);
            if (indexOf != -1) {
                indexNext = indexOf + founder.length();
                count++;
            } else {
                indexNext = indexOf;
            }
        }

        return count;
    }
}
