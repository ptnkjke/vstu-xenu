package vstu.gui.forms.main;

/**
 * Created by Lopatin on 22.03.14.
 */
public class DataTable {
    private String address;
    private String statusColumn;
    private Integer lvlColumn;
    private String typeColumn;
    private String charset;

    public DataTable(String address,
                     String statusColumn,
                     Integer lvlColumn,
                     String typeColumn,
                     String charset) {

        this.address = address;
        this.statusColumn = statusColumn;
        this.lvlColumn = lvlColumn;
        this.typeColumn = typeColumn;
        this.charset = charset;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatusColumn() {
        return statusColumn;
    }

    public void setStatusColumn(String statusColumn) {
        this.statusColumn = statusColumn;
    }

    public Integer getLvlColumn() {
        return lvlColumn;
    }

    public void setLvlColumn(Integer lvlColumn) {
        this.lvlColumn = lvlColumn;
    }

    public String getTypeColumn() {
        return typeColumn;
    }

    public void setTypeColumn(String typeColumn) {
        this.typeColumn = typeColumn;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
