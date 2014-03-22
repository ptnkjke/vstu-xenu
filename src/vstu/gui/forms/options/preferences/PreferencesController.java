package vstu.gui.forms.options.preferences;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import vstu.gui.data.Options;

/**
 * Created by Lopatin on 22.03.2014.
 */
public class PreferencesController {
    @FXML
    private Button imgButton;
    @FXML
    private Button cssButton;
    @FXML
    private Button urlButton;
    @FXML
    private Button jsButton;
    @FXML
    private Button excludeButton;

    @FXML
    private TextField timeoutTF;

    @FXML
    private void initialize() {
        if (Options.urlSelectorEnabled) {
            urlButton.setText("ON");
            urlButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            urlButton.setText("OFF");
            urlButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (Options.imgSelectorEnabled) {
            imgButton.setText("ON");
            imgButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            imgButton.setText("OFF");
            imgButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (Options.cssSelectorEnabled) {
            cssButton.setText("ON");
            cssButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            cssButton.setText("OFF");
            cssButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (Options.jsSelectorEnabled) {
            jsButton.setText("ON");
            jsButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            jsButton.setText("OFF");
            jsButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (Options.excludeRepeatedUrl) {
            excludeButton.setText("ON");
            excludeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            excludeButton.setText("OFF");
            excludeButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        timeoutTF.setText(Integer.toString(Options.timeout));
    }

    public void onUrlButtonAction() {
        if (Options.urlSelectorEnabled) {
            Options.urlSelectorEnabled = false;
            urlButton.setText("OFF");
            urlButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            urlButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            Options.urlSelectorEnabled = true;
            urlButton.setText("ON");
            urlButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            urlButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onCssButtonAction() {
        if (Options.cssSelectorEnabled) {
            Options.cssSelectorEnabled = false;
            cssButton.setText("OFF");
            cssButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            cssButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            Options.cssSelectorEnabled = true;
            cssButton.setText("ON");
            cssButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            cssButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onImgButtonAction() {
        if (Options.imgSelectorEnabled) {
            Options.imgSelectorEnabled = false;
            imgButton.setText("OFF");
            imgButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            imgButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            Options.imgSelectorEnabled = true;
            imgButton.setText("ON");
            imgButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            imgButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onJSButtonAction() {
        if (Options.jsSelectorEnabled) {
            Options.jsSelectorEnabled = false;
            jsButton.setText("OFF");
            jsButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            jsButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            Options.jsSelectorEnabled = true;
            jsButton.setText("ON");
            jsButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            jsButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onExcludeButtonAction() {
        if (Options.excludeRepeatedUrl) {
            Options.excludeRepeatedUrl = false;
            excludeButton.setText("OFF");
            excludeButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            excludeButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            Options.excludeRepeatedUrl = true;
            excludeButton.setText("ON");
            excludeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            excludeButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onTimeoutKeyEvent() {
        Options.timeout = Integer.parseInt(timeoutTF.getText());
    }
}
