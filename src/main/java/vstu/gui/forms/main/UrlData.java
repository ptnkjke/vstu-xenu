package vstu.gui.forms.main;

/**
 * Created by Lopatin on 22.03.14.
 */

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

    public UrlData(String address,
                   String statusColumn,
                   Integer lvl,
                   String type,
                   String charset,
                   Integer size,
                   String parentUrl) {

        this.address = address;
        this.status = statusColumn;
        this.lvl = lvl;
        this.type = type;
        this.charset = charset;
        this.size = size;
        this.parentUrl = parentUrl;
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
}
