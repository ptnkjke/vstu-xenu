package vstu.gui.forms.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import vstu.gui.data.Options;
import vstu.gui.forms.options.preferences.PreferencesController;
import vstu.gui.logic.Parser;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


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
    private Parser parser;


    @FXML
    private void initialize() {
        // Инициализация таблици
        addressColumn.setCellValueFactory(new PropertyValueFactory<DataTable, String>("address"));

        // Добавление возможности, чтобы по клику ячейки открывался браузер
        addressColumn.setCellFactory(new Callback<TableColumn<DataTable, String>, TableCell<DataTable, String>>() {
            @Override
            public TableCell<DataTable, String> call(TableColumn<DataTable, String> dataTableStringTableColumn) {
                TableCell cell = new TableCell<DataTable, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                    }
                };
                // При клике открытие браузера
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        TableCell tc = (TableCell) mouseEvent.getSource();
                        String url = tc.getText();
                        if (url != null) {
                            Desktop desktop;
                            if (Desktop.isDesktopSupported()) {
                                desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                    // launch browser
                                    URI uri;
                                    try {
                                        uri = new URI(url);
                                        desktop.browse(uri);
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    } catch (URISyntaxException use) {
                                        use.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });

                cell.setStyle("-fx-text-fill: darkblue; -fx-underline: true");
                return cell;
            }
        });

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
            parser = new Parser();
            parser.tableWorker = this;
            startButton.setText("Stop");
            dataTables.clear();
            isStartPasring = true;
            parser.startCheck(url);
        } else {
            for (Thread thread : parser.list) {
                thread.stop();
            }
            parser.list.clear();
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
