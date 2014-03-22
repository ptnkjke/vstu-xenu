package vstu.gui.forms.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import vstu.gui.logic.Parser;

import java.io.IOException;


/**
 * Created by Lopatin on 21.03.14.
 */
public class MainFormController implements ITableWorker {
    private ObservableList<DataTable> dataTables = FXCollections.observableArrayList();
    @FXML
    private TableView<DataTable> tableTW;
    @FXML
    private TableColumn<DataTable, String> addressColumn;
    @FXML
    private TableColumn<DataTable, String> statusColumn;
    @FXML
    private TableColumn<DataTable, Integer> lvlColumn;
    @FXML
    private TableColumn<DataTable, String> typeColumn;
    @FXML
    private TableColumn<DataTable, String> charsetColumn;
    @FXML
    private TextField urlTF;
    @FXML
    private Label urlCountInQueueLabel;
    @FXML
    private Label urlCountInTable;

    /**
     * Объект для синхронизации
     */
    private Object sync = new Object();

    @FXML
    private void initialize() {
        // Инициализация таблици
        addressColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("address"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("statusColumn"));
        lvlColumn.setCellValueFactory(new PropertyValueFactory<DataTable, Integer>("lvlColumn"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("typeColumn"));
        charsetColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("charset"));
        tableTW.setItems(dataTables);
        tableTW.setPlaceholder(new Text("VSTU XENU 2014"));
    }

    public void onPreferencesMenuItemAction() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../options/preferences/PreferencesForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        dialog.show();
        dialog.setTitle("Preferences");
    }

    public void onStartButtonAction() {
        String url = urlTF.getText();
        Parser.tableWorker = this;
        Parser.startCheck(url);
    }

    @Override
    public void addRow(final DataTable dt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dataTables.add(dt);
                urlCountInTable.setText(Integer.toString(dataTables.size()));
            }
        });
    }

    @Override
    public void updateCountUrlInQueue(final int count) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                urlCountInQueueLabel.setText(Integer.toString(count));
            }
        });
    }
}
