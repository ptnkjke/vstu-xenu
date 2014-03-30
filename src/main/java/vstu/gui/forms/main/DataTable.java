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
    private Integer size;

    public DataTable(String address,
                     String statusColumn,
                     Integer lvlColumn,
                     String typeColumn,
                     String charset,
                     Integer size) {

        this.address = address;
        this.statusColumn = statusColumn;
        this.lvlColumn = lvlColumn;
        this.typeColumn = typeColumn;
        this.charset = charset;
        this.size = size;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
