package vstu.gui.forms.options.preferences;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import vstu.gui.data.OptionsProperties;

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
        if (OptionsProperties.urlSelectorEnabled) {
            urlButton.setText("ON");
            urlButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            urlButton.setText("OFF");
            urlButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.imgSelectorEnabled) {
            imgButton.setText("ON");
            imgButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            imgButton.setText("OFF");
            imgButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.cssSelectorEnabled) {
            cssButton.setText("ON");
            cssButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            cssButton.setText("OFF");
            cssButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.jsSelectorEnabled) {
            jsButton.setText("ON");
            jsButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            jsButton.setText("OFF");
            jsButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.excludeRepeatedUrl) {
            excludeButton.setText("ON");
            excludeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            excludeButton.setText("OFF");
            excludeButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        timeoutTF.setText(Integer.toString(OptionsProperties.timeout));
    }

    public void onUrlButtonAction() {
        if (OptionsProperties.urlSelectorEnabled) {
            OptionsProperties.urlSelectorEnabled = false;
            urlButton.setText("OFF");
            urlButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            urlButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.urlSelectorEnabled = true;
            urlButton.setText("ON");
            urlButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            urlButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onCssButtonAction() {
        if (OptionsProperties.cssSelectorEnabled) {
            OptionsProperties.cssSelectorEnabled = false;
            cssButton.setText("OFF");
            cssButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            cssButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.cssSelectorEnabled = true;
            cssButton.setText("ON");
            cssButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            cssButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onImgButtonAction() {
        if (OptionsProperties.imgSelectorEnabled) {
            OptionsProperties.imgSelectorEnabled = false;
            imgButton.setText("OFF");
            imgButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            imgButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.imgSelectorEnabled = true;
            imgButton.setText("ON");
            imgButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            imgButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onJSButtonAction() {
        if (OptionsProperties.jsSelectorEnabled) {
            OptionsProperties.jsSelectorEnabled = false;
            jsButton.setText("OFF");
            jsButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            jsButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.jsSelectorEnabled = true;
            jsButton.setText("ON");
            jsButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            jsButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onExcludeButtonAction() {
        if (OptionsProperties.excludeRepeatedUrl) {
            OptionsProperties.excludeRepeatedUrl = false;
            excludeButton.setText("OFF");
            excludeButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            excludeButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.excludeRepeatedUrl = true;
            excludeButton.setText("ON");
            excludeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            excludeButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onTimeoutKeyEvent() {
        OptionsProperties.timeout = Integer.parseInt(timeoutTF.getText());
    }
}
