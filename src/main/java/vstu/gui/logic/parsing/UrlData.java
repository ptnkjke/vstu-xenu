package vstu.gui.logic.parsing;

/**
 * Created by Lopatin on 20.03.14.
 */
public class UrlData {
    private int lvl;
    private String url;

    public UrlData(String url, int lvl) {
        this.lvl = lvl;
        this.url = url;
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
}
