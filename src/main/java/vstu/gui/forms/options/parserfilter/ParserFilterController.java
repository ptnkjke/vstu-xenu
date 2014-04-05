package vstu.gui.forms.options.parserfilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    private ListView<String> searchList;
    @FXML
    private ListView<String> tagList;
    @FXML
    private ListView<String> includeList;
    @FXML
    private ListView<String> excludeList;


    public ListView<String> getSearchList() {
        return searchList;
    }

    public ListView<String> getTagList() {
        return tagList;
    }

    public ListView<String> getIncludeList() {
        return includeList;
    }

    public ListView<String> getExcludeList() {
        return excludeList;
    }


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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> data = FXCollections.observableArrayList();
        this.searchList.setItems(data);

        data = FXCollections.observableArrayList();
        this.tagList.setItems(data);
        data = FXCollections.observableArrayList();
        this.includeList.setItems(data);
        data = FXCollections.observableArrayList();
        this.excludeList.setItems(data);

        this.searchList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                ListCell cell = new ListCell<String>() {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);
                        this.setText(s);
                    }
                };

                cell.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        ListCell lc = (ListCell) keyEvent.getSource();
                        ListView lv = lc.getListView();
                        lv.getItems().remove(lc);
                    }
                });
                return cell;
            }
        });

        for (String string : ParserFilter.searchList) {
            this.searchList.getItems().add(string);

        }
        for (String string : ParserFilter.tagList) {
            this.tagList.getItems().add(string);
        }
        for (String string : ParserFilter.includeList) {
            this.includeList.getItems().add(string);
        }
        for (String string : ParserFilter.exludeList) {
            this.excludeList.getItems().add(string);
        }
    }

    public void save() {
        ParserFilter.searchList.clear();
        for (String string : this.searchList.getItems()) {
            ParserFilter.searchList.add(string);
        }
        ParserFilter.tagList.clear();
        for (String string : this.tagList.getItems()) {
            ParserFilter.tagList.add(string);
        }
        ParserFilter.includeList.clear();
        for (String string : this.includeList.getItems()) {
            ParserFilter.includeList.add(string);
        }
        ParserFilter.exludeList.clear();
        for (String string : this.excludeList.getItems()) {
            ParserFilter.exludeList.add(string);
        }
    }

    public void onAddTextSearchBAction() {
        if (textSearchTB.getText().length() > 0) {
            searchList.getItems().add(textSearchTB.getText());
            textSearchTB.setText("");
        }
    }

    public void onAddTagSearchB() {
        if (tagSearchTB.getText().length() > 0) {
            tagList.getItems().add(tagSearchTB.getText());
            tagSearchTB.setText("");
        }
    }

    public void onAddIncludeUrlSearchB() {
        if (includeUrlTB.getText().length() > 0) {
            includeList.getItems().add(includeUrlTB.getText());
            includeUrlTB.setText("");
        }
    }

    public void onAddExcludeUrlSearchB() {
        if (excludeUrlTB.getText().length() > 0) {
            excludeList.getItems().add(excludeUrlTB.getText());
            excludeUrlTB.setText("");
        }
    }
}
