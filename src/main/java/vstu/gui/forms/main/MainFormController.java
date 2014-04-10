package vstu.gui.forms.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;
import vstu.gui.data.OptionsProperties;
import vstu.gui.forms.options.parserfilter.ParserFilterController;
import vstu.gui.forms.options.preferences.PreferencesController;
import vstu.gui.logic.export.HtmlExport;
import vstu.gui.logic.parsing.Parser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Lopatin on 21.03.14.
 */
public class MainFormController implements ITableWorker {
    /**
     * Список обработанных url, да и по совместительству представление в таблице
     */
    private ObservableList<UrlData> dataTables = FXCollections.observableArrayList();
    @FXML
    private TableView<UrlData> tableTW;
    @FXML
    private TableColumn<UrlData, String> addressColumn;
    @FXML
    private TableColumn<UrlData, String> statusColumn;
    @FXML
    private TableColumn<UrlData, Integer> lvlColumn;
    @FXML
    private TableColumn<UrlData, String> typeColumn;
    @FXML
    private TableColumn<UrlData, String> charsetColumn;
    @FXML
    private TableColumn<UrlData, String> sizeColumn;
    @FXML
    private TextField urlTF;
    @FXML
    private Label urlCountInQueueLabel;
    @FXML
    private Label urlCountInTable;
    @FXML
    private Button startButton;

    /**
     * Начался ли разбор страни?
     */
    private boolean isStartPasring = false;
    /**
     * Объект для парсера
     */
    private Parser parser;
    /**
     * Объект для таймера
     */
    private Timer timer;


    @FXML
    private void initialize() {
        // Инициализация таблици
        addressColumn.setCellValueFactory(new PropertyValueFactory<UrlData, String>("address"));

        // Добавление возможности, чтобы по клику ячейки открывался браузер
        addressColumn.setCellFactory(new Callback<TableColumn<UrlData, String>, TableCell<UrlData, String>>() {
            @Override
            public TableCell<UrlData, String> call(TableColumn<UrlData, String> dataTableStringTableColumn) {
                TableCell cell = new TableCell<UrlData, String>() {
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

        statusColumn.setCellValueFactory(new PropertyValueFactory<UrlData, String>("status"));
        lvlColumn.setCellValueFactory(new PropertyValueFactory<UrlData, Integer>("lvl"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<UrlData, String>("type"));
        charsetColumn.setCellValueFactory(new PropertyValueFactory<UrlData, String>("charset"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<UrlData, String>("size"));
        tableTW.setItems(dataTables);
        tableTW.setPlaceholder(new Text("VSTU XENU 2014"));
    }

    public void onSaveAsHtmlMenuItemAction() {
        // Если обработка завершена

        // Показать диалог для сохранения файла
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML", "*.html"));

        File file = chooser.showSaveDialog(null);

        if (file == null) {
            return;
        }

        // Сохранить в файл
        HtmlExport export = new HtmlExport();
        export.createResultDoc(dataTables, file);
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
                OptionsProperties.save();
            }
        });

        root.getStylesheets().add("/vstu/gui/forms/table.css");
        dialog.show();
    }

    public void onParserFilterItemAction() {
        Parent root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/vstu/gui/forms/options/parserfilter/ParserFilter.fxml"));
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            root = (Parent) fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        final Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));

        dialog.setTitle("Preferences");

        final FXMLLoader finalFxmlLoader = fxmlLoader;

        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ((ParserFilterController) finalFxmlLoader.getController()).save();
            }
        });
        final ParserFilterController controller = finalFxmlLoader.getController();

        // Удаление строки по нажатию DELETE
        controller.getIncludeList().getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.DELETE), new Runnable() {
            @Override
            public void run() {

                int num = controller.getIncludeList().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getIncludeList().isFocused()) {
                    controller.getIncludeList().getItems().remove(num);
                }
                num = controller.getExcludeList().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getExcludeList().isFocused()) {
                    controller.getExcludeList().getItems().remove(num);
                }
                num = controller.getSearchList().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getSearchList().isFocused()) {
                    controller.getSearchList().getItems().remove(num);
                }
                num = controller.getTagList().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getTagList().isFocused()) {
                    controller.getTagList().getItems().remove(num);
                }
            }
        });

        // Добавление записи в список по нажатию ENTER
        controller.getIncludeList().getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
            @Override
            public void run() {

                TextField tf;
                tf = controller.getExcludeUrlTB();
                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getExcludeList().getItems().add(text);
                        tf.setText("");
                    }
                }

                tf = controller.getIncludeUrlTB();
                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getIncludeList().getItems().add(text);
                        tf.setText("");
                    }
                }

                tf = controller.getTagSearchTB();
                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getTagList().getItems().add(text);
                        tf.setText("");
                    }
                }
                tf = controller.getTextSearchTB();

                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getSearchList().getItems().add(text);
                        tf.setText("");
                    }
                }
            }
        });
        root.getStylesheets().add("/vstu/gui/forms/table.css");
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

            // Запускаем таймер, который бы отслеживал состояние потоков для вывода соощений о завершении обработки
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (parser.isFinish()) {
                                timer.cancel();
                                onStop();

                                // Показывае финишное окошечко
                                Parent root = null;
                                FXMLLoader fxmlLoader = null;
                                try {
                                    fxmlLoader = new FXMLLoader(getClass().getResource("/vstu/gui/forms/popups/FinishForm.fxml"));
                                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

                                    root = (Parent) fxmlLoader.load();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                final Stage dialog = new Stage();
                                dialog.initStyle(StageStyle.UTILITY);
                                dialog.setResizable(false);
                                dialog.initModality(Modality.APPLICATION_MODAL);
                                dialog.setScene(new Scene(root));

                                dialog.setTitle("Finish");

                                urlCountInQueueLabel.setText(Integer.toString(0));
                                dialog.show();
                            }
                        }
                    });
                }
            }, OptionsProperties.timeout + 2000, OptionsProperties.timeout + 2000);

        } else {
            onStop();
        }
    }

    private void onStop() {
        for (Thread thread : parser.threads) {
            thread.interrupt();
        }

        parser.threads.clear();
        if (parser.mainThread != null) {
            Thread t = parser.mainThread;
            t.interrupt();
        }

        isStartPasring = false;
        startButton.setText("Start");

        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void addRow(final UrlData dt) {
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
