package vstu.gui.forms.main;

/**
 * Created by Lopatin on 22.03.2014.
 */
public interface ITableWorker {
    /**
     * Добавление строки в таблицу
     *
     * @param dt
     */
    public void addRow(UrlData dt);

    /**
     * Обновление количества записей, которые стоят в очереди на проверку
     *
     * @param count
     */
    public void updateCountUrlInQueue(int count);
}
