package vstu.gui.forms.main;

/**
 * Created by Lopatin on 22.03.14.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Представление данных о url
 */
public class UrlData {
    /**
     * Само значение url
     */
    private String address;
    /**
     * Статус
     */
    private String status;
    /**
     * Уровень погружённости
     */
    private Integer lvl;
    /**
     * Mime-type
     */
    private String type;
    /**
     * Кодировка
     */
    private String charset;
    /**
     * Размер в байтах
     */
    private Integer size;
    /**
     * Родительская ссылка
     */
    private String parentUrl;
    /**
     * Содержит теги, которые нужно
     */
    private Map<String, String> containsTag = new HashMap<>();
    /**
     * Содержит в себе подстроку
     */
    private Map<String, Integer> containsText = new HashMap<>();
    /**
     * Время загрузки ресурса
     */
    private long timeLoad;

    public UrlData(String address,
                   String statusColumn,
                   Integer lvl,
                   String type,
                   String charset,
                   Integer size,
                   String parentUrl,
                   long timeLoad) {

        this.address = address;
        this.status = statusColumn;
        this.lvl = lvl;
        this.type = type;
        this.charset = charset;
        this.size = size;
        this.parentUrl = parentUrl;
        this.timeLoad = timeLoad;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLvl() {
        return lvl;
    }

    public void setLvl(Integer lvl) {
        this.lvl = lvl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public Map<String, String> getContainsTag() {
        return containsTag;
    }

    public void setContainsTag(Map<String, String> containsTag) {
        this.containsTag = containsTag;
    }

    public Map<String, Integer> getContainsText() {
        return containsText;
    }

    public void setContainsText(Map<String, Integer> containsText) {
        this.containsText = containsText;
    }

    public long getTimeLoad() {
        return timeLoad;
    }

    public void setTimeLoad(long timeLoad) {
        this.timeLoad = timeLoad;
    }
}
