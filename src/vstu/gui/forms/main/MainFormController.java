package vstu.gui.forms.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import vstu.gui.data.Options;
import vstu.gui.forms.options.preferences.PreferencesController;
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
    private TableColumn<DataTable, String> sizeColumn;
    @FXML
    private TextField urlTF;
    @FXML
    private Label urlCountInQueueLabel;
    @FXML
    private Label urlCountInTable;
    @FXML
    private Button startButton;

    private boolean isStartPasring = false;


    @FXML
    private void initialize() {
        // Инициализация таблици
        addressColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("address"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("statusColumn"));
        lvlColumn.setCellValueFactory(new PropertyValueFactory<DataTable, Integer>("lvlColumn"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("typeColumn"));
        charsetColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("charset"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("size"));
        tableTW.setItems(dataTables);
        tableTW.setPlaceholder(new Text("VSTU XENU 2014"));
    }

    public void onPreferencesMenuItemAction() {
        Parent root = null;
        try {
            root = FXMLLoader.load(PreferencesController.class.getResource("/vstu/gui/forms/options/preferences/PreferencesForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        dialog.setTitle("Preferences");

        // Событие на закрытие окна
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Options.save();
            }
        });

        dialog.show();
    }

    public void onStartButtonAction() {
        if (!isStartPasring) {
            String url = urlTF.getText();
            Parser.tableWorker = this;
            startButton.setText("Stop");
            dataTables.clear();
            isStartPasring = true;
            Parser.startCheck(url);
        } else {
            for (Thread thread : Parser.list) {
                thread.stop();
            }
            Parser.list.clear();
            isStartPasring = false;
            startButton.setText("Start");
        }
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
