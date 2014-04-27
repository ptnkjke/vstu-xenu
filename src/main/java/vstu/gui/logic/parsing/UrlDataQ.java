package vstu.gui.logic.parsing;

import vstu.gui.forms.main.UrlData;

/**
 * Created by Lopatin on 20.03.14.
 */
public class UrlDataQ {
    /**
     * Текущий обрабатываемый уровень
     */
    private int lvl;
    /**
     * Ссылка, которую нужно обработать
     */
    private String url;
    /**
     * Родитель
     */
    private UrlData parent;

    public UrlDataQ(String url, int lvl, UrlData parent) {
        this.lvl = lvl;
        this.url = url;
        this.parent = parent;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlData getParent() {
        return parent;
    }

    public void setParent(UrlData parent) {
        this.parent = parent;
    }
}
