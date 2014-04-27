package vstu.gui.forms.main.tableoptions;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lopatin on 27.04.2014.
 */
public class TableColumnOption {

    public static void show(TableView tw) {
        ObservableList<TableColumn> list = tw.getColumns();

        List<TableColumn> columns = new ArrayList<>();
        for (TableColumn tc : list) {
            columns.add(tc);
        }

        GridPane gridPane = getTableColumnOptionGrid(columns);


        Scene scene = new Scene(gridPane, 100, 500);

        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setMaxWidth(100);
        stage.setResizable(true);
        stage.show();
    }

    public static GridPane getTableColumnOptionGrid(List<TableColumn> columns) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(1);
        gridPane.setHgap(columns.size());

        int i = 0;
        for (final TableColumn column : columns) {
            final CheckBox cb = new CheckBox();

            cb.setText(column.getText());
            if (column.isVisible()) {
                cb.setSelected(column.isVisible());
            }

            // TODO: Добавить листенеры на переключение selected
            cb.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!cb.isSelected()) {
                        column.setVisible(false);
                    } else {
                        column.setVisible(true);
                    }
                }
            });

            gridPane.add(cb, 0, i);
            i++;
        }


        return gridPane;
    }

}
