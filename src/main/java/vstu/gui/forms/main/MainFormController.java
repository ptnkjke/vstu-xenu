package vstu.gui.forms.main;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import vstu.gui.data.OptionsProperties;
import vstu.gui.data.ParserFilter;
import vstu.gui.forms.main.tableoptions.TableColumnOption;
import vstu.gui.forms.options.parserfilter.ParserFilterController;
import vstu.gui.logic.export.html.HtmlExport;
import vstu.gui.logic.export.sitemap.SiteMapGen;
import vstu.gui.logic.parsing.Parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
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
    private TableColumn<UrlData, String> timeLoad;
    @FXML
    private TextField urlTF;
    @FXML
    private Label urlCountInQueueLabel;
    @FXML
    private Label urlCountInTable;
    @FXML
    private Button startButton;

    /**
     * Главный элемент
     */
    private UrlData root;

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
                        if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                            return;
                        }
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
        timeLoad.setCellValueFactory(new PropertyValueFactory<UrlData, String>("timeLoad"));
        tableTW.setItems(dataTables);
        tableTW.setPlaceholder(new Text("VSTU XENU 2014"));


        tableTW.addEventFilter(MouseEvent.MOUSE_CLICKED, new MContext());

        tableTW.getSelectionModel().setCellSelectionEnabled(true);
        tableTW.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        String visible = OptionsProperties.visibleColumns;
        if (visible.charAt(0) == '0') {
            addressColumn.setVisible(false);
        }
        if (visible.charAt(1) == '0') {
            statusColumn.setVisible(false);
        }
        if (visible.charAt(2) == '0') {
            lvlColumn.setVisible(false);
        }
        if (visible.charAt(3) == '0') {
            typeColumn.setVisible(false);
        }
        if (visible.charAt(4) == '0') {
            charsetColumn.setVisible(false);
        }
        if (visible.charAt(5) == '0') {
            sizeColumn.setVisible(false);
        }
        if (visible.charAt(6) == '0') {
            timeLoad.setVisible(false);
        }
    }

    public String getDataString() {
        StringBuilder data = new StringBuilder();

        if (!addressColumn.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }
        if (!statusColumn.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }
        if (!lvlColumn.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }
        if (!typeColumn.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }
        if (!charsetColumn.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }
        if (!sizeColumn.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }
        if (!timeLoad.isVisible()) {
            data.append("0");
        } else {
            data.append("1");
        }

        return data.toString();
    }

    private class MContext implements EventHandler<MouseEvent> {
        private ContextMenu menu = null;

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (menu != null) {
                menu.hide();
            }

            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                return;
            }

            final TableView tw = (TableView) mouseEvent.getSource();
            // TODO: SCENE BUILDER по какой-то причине глючит

            menu = new ContextMenu();
            MenuItem copyItem = new MenuItem("Copy");

            copyItem.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent actionEvent) {
                    ObservableList<TablePosition> list = tw.getSelectionModel().getSelectedCells();

                    StringBuilder sb = new StringBuilder();
                    for (TablePosition tp : list) {
                        sb.append(tp.getTableColumn().getCellData(tp.getRow()));
                    }

                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(sb.toString());
                    clipboard.setContent(content);
                }
            });

            menu.getItems().add(copyItem);
            ;

            menu.show(tableTW, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
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

        if (!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + ".html");
        }

        // Сохранить в файл
        HtmlExport export = new HtmlExport();
        export.createResultDoc(dataTables, file);
    }

    public void onSaveSitemapItemAction() {
        // Если обработка завершена

        // Показать диалог для сохранения файла
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));

        File file = chooser.showSaveDialog(null);

        if (file == null) {
            return;
        }

        if (!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + ".xml");
        }

        // Сохранить в файл

        SiteMapGen siteMapGen = new SiteMapGen();
        try {
            siteMapGen.createSiteMap(file, dataTables);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void onPreferencesMenuItemAction() {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setResources(ResourceBundle.getBundle("translation"));

            root = (Parent) fxmlLoader.load(this.getClass().getResource("/vstu/gui/forms/options/preferences/PreferencesForm.fxml").openStream());
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
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setResources(ResourceBundle.getBundle("translation"));
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            root = (Parent) fxmlLoader.load(getClass().getResource("/vstu/gui/forms/options/parserfilter/ParserFilter.fxml").openStream());

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
        controller.getIncludeUrlTable().getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.DELETE), new Runnable() {
            @Override
            public void run() {

                int num = controller.getIncludeUrlTable().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getIncludeUrlTable().isFocused()) {
                    controller.getIncludeUrlTable().getItems().remove(num);
                }
                num = controller.getExcludeUrlTable().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getExcludeUrlTable().isFocused()) {
                    controller.getExcludeUrlTable().getItems().remove(num);
                }
                num = controller.getTagSearchList().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getTagSearchList().isFocused()) {
                    controller.getTagSearchList().getItems().remove(num);
                }
                num = controller.getTextSearchList().getSelectionModel().getSelectedIndex();
                if (num != -1 && controller.getTextSearchList().isFocused()) {
                    controller.getTextSearchList().getItems().remove(num);
                }
            }
        });

        // Добавление записи в список по нажатию ENTER
        controller.getIncludeUrlTable().getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
            @Override
            public void run() {

                TextField tf;
                tf = controller.getExcludeUrlTB();
                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getExcludeUrlTable().getItems().add(new ParserFilter.Data(text));
                        tf.setText("");
                    }
                }

                tf = controller.getIncludeUrlTB();
                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getIncludeUrlTable().getItems().add(new ParserFilter.Data(text));
                        tf.setText("");
                    }
                }

                tf = controller.getTagSearchTB();
                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getTagSearchList().getItems().add(text);
                        tf.setText("");
                    }
                }
                tf = controller.getTextSearchTB();

                if (tf.isFocused()) {
                    String text = tf.getText();
                    if (!text.isEmpty()) {
                        controller.getTextSearchList().getItems().add(text);
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

            // Добавляем столбцы, согласно фильтру
            ///////////////////////////////////
            for (final String string : ParserFilter.searchList) {
                final TableColumn<UrlData, String> column = new TableColumn<>();
                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<UrlData, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<UrlData, String> urlDataStringCellDataFeatures) {
                        final UrlData urlData = urlDataStringCellDataFeatures.getValue();

                        Integer counter = urlData.getContainsText().get(string);
                        String res = "";
                        if (counter != null) {
                            res = counter.toString();
                        }
                        return new SimpleStringProperty(res);
                    }
                });
                column.setText(string);
                tableTW.getColumns().add(column);
            }

            for (final String string : ParserFilter.tagList) {
                TableColumn<UrlData, String> column = new TableColumn<>();
                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<UrlData, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<UrlData, String> urlDataStringCellDataFeatures) {
                        final UrlData urlData = urlDataStringCellDataFeatures.getValue();

                        String res = urlData.getContainsTag().get(string);
                        if (res == null) {
                            res = "";
                        }
                        return new SimpleStringProperty(res);
                    }
                });
                column.setText(string);
                tableTW.getColumns().add(column);
            }
            ///////////////////////////////////
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

    public void onColumnVisibleAction() {
        TableColumnOption.show(tableTW, this);
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
                if (root != null) {
                    root = dt;
                }

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
