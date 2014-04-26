package vstu.gui.forms.options.parserfilter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import vstu.gui.data.ParserFilter;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Lopatin on 22.03.2014.
 */
public class ParserFilterController implements Initializable {
    @FXML
    private TextField textSearchTB;
    @FXML
    private TextField tagSearchTB;
    @FXML
    private TextField includeUrlTB;
    @FXML
    private TextField excludeUrlTB;

    @FXML
    private ListView<String> textSearchList;
    @FXML
    private ListView<String> tagSearchList;
    @FXML
    private TableView<ParserFilter.Data> includeUrlTable;
    @FXML
    private TableView<ParserFilter.Data> excludeUrlTable;

    private ObservableList<String> textSearchItems = FXCollections.observableArrayList();
    private ObservableList<String> tagSearchItems = FXCollections.observableArrayList();
    private ObservableList<ParserFilter.Data> includeUrlItems = FXCollections.observableArrayList();
    private ObservableList<ParserFilter.Data> excludeUrlItems = FXCollections.observableArrayList();


    public TextField getTextSearchTB() {
        return textSearchTB;
    }

    public TextField getTagSearchTB() {
        return tagSearchTB;
    }

    public TextField getIncludeUrlTB() {
        return includeUrlTB;
    }

    public TextField getExcludeUrlTB() {
        return excludeUrlTB;
    }

    public TableView<ParserFilter.Data> getExcludeUrlTable() {
        return excludeUrlTable;
    }

    public TableView<ParserFilter.Data> getIncludeUrlTable() {
        return includeUrlTable;
    }

    public ListView<String> getTagSearchList() {
        return tagSearchList;
    }

    public ListView<String> getTextSearchList() {
        return textSearchList;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (String data : ParserFilter.searchList) {
            textSearchItems.add(data);
        }
        for (String data : ParserFilter.tagList) {
            tagSearchItems.add(data);
        }
        for (ParserFilter.Data data : ParserFilter.includeList) {
            includeUrlItems.add(data);
        }
        for (ParserFilter.Data data : ParserFilter.exludeList) {
            excludeUrlItems.add(data);
        }

        textSearchList.setItems(textSearchItems);
        tagSearchList.setItems(tagSearchItems);
        includeUrlTable.setItems(includeUrlItems);
        excludeUrlTable.setItems(excludeUrlItems);

        includeUrlTable.setPlaceholder(new Text(""));
        excludeUrlTable.setPlaceholder(new Text(""));

        TableColumn<ParserFilter.Data, String> column = (TableColumn<ParserFilter.Data, String>) includeUrlTable.getColumns().get(0);
        column.setCellValueFactory(new PropertyValueFactory<ParserFilter.Data, String>("data"));

        column = (TableColumn<ParserFilter.Data, String>) excludeUrlTable.getColumns().get(0);
        column.setCellValueFactory(new PropertyValueFactory<ParserFilter.Data, String>("data"));

        TableColumn<ParserFilter.Data, CheckBox> column2 = (TableColumn<ParserFilter.Data, CheckBox>) includeUrlTable.getColumns().get(1);
        column2.setCellValueFactory(new CheckBoxColumn());

        column2 = (TableColumn<ParserFilter.Data, CheckBox>) excludeUrlTable.getColumns().get(1);
        column2.setCellValueFactory(new CheckBoxColumn());
    }

    private class CheckBoxColumn implements Callback<TableColumn.CellDataFeatures<ParserFilter.Data, CheckBox>, ObservableValue<CheckBox>> {

        @Override
        public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<ParserFilter.Data, CheckBox> dataCheckBoxCellDataFeatures) {
            final CheckBox checkBox = new CheckBox();
            final ParserFilter.Data data = dataCheckBoxCellDataFeatures.getValue();
            checkBox.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    data.setRegexp(checkBox.isSelected());
                }
            });

            checkBox.setSelected(data.isRegexp());
            return new SimpleObjectProperty<CheckBox>(checkBox);
        }
    }

    public void save() {
        ParserFilter.searchList.clear();
        for (String data : this.textSearchList.getItems()) {
            ParserFilter.searchList.add(data);
        }
        ParserFilter.tagList.clear();
        for (String data : this.tagSearchList.getItems()) {
            ParserFilter.tagList.add(data);
        }
        ParserFilter.includeList.clear();
        for (ParserFilter.Data data : includeUrlItems) {
            ParserFilter.includeList.add(data);
        }
        ParserFilter.exludeList.clear();
        for (ParserFilter.Data data : excludeUrlItems) {
            ParserFilter.exludeList.add(data);
        }
    }

    public void onAddTextSearchBAction() {
        if (textSearchTB.getText().length() > 0) {
            textSearchList.getItems().add(textSearchTB.getText());
            textSearchTB.setText("");
        }
    }

    public void onAddTagSearchB() {
        if (tagSearchTB.getText().length() > 0) {
            tagSearchList.getItems().add(tagSearchTB.getText());
            tagSearchTB.setText("");
        }
    }

    public void onAddIncludeUrlSearchB() {
        if (includeUrlTB.getText().length() > 0) {
            includeUrlTable.getItems().add(new ParserFilter.Data(includeUrlTB.getText()));
            includeUrlTB.setText("");
        }
    }

    public void onAddExcludeUrlSearchB() {
        if (excludeUrlTB.getText().length() > 0) {
            excludeUrlTable.getItems().add(new ParserFilter.Data(excludeUrlTB.getText()));
            excludeUrlTB.setText("");
        }
    }
}
