package vstu.gui.forms.options.preferences;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import vstu.gui.data.Language;
import vstu.gui.data.OptionsProperties;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ResourceBundle;

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
    private Button mBeyondButton;
    @FXML
    private Button mBeyondSubDomainButton;

    @FXML
    private TextField timeoutTF;
    @FXML
    private Slider threadCounter;
    @FXML
    private ComboBox<String> language;


    private String str_on = ResourceBundle.getBundle("translation").getString("preferenceform.on");
    private String str_off = ResourceBundle.getBundle("translation").getString("preferenceform.off");

    @FXML
    private void initialize() {
        if (OptionsProperties.urlSelectorEnabled) {
            urlButton.setText(str_on);
            urlButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            urlButton.setText(str_off);
            urlButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.imgSelectorEnabled) {
            imgButton.setText(str_on);
            imgButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            imgButton.setText(str_off);
            imgButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.cssSelectorEnabled) {
            cssButton.setText(str_on);
            cssButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            cssButton.setText(str_off);
            cssButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.jsSelectorEnabled) {
            jsButton.setText(str_on);
            jsButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            jsButton.setText(str_off);
            jsButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.excludeRepeatedUrl) {
            excludeButton.setText(str_on);
            excludeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            excludeButton.setText(str_off);
            excludeButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.movingBeyond) {
            mBeyondButton.setText(str_on);
            mBeyondButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            mBeyondButton.setText(str_off);
            mBeyondButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        if (OptionsProperties.movingSubDomain) {
            mBeyondSubDomainButton.setText(str_on);
            mBeyondSubDomainButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        } else {
            mBeyondSubDomainButton.setText(str_off);
            mBeyondSubDomainButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
        }

        timeoutTF.setText(Integer.toString(OptionsProperties.timeout));

        language.getItems().add(ResourceBundle.getBundle("translation").getString("preferenceform.language.russian"));
        language.getItems().add(ResourceBundle.getBundle("translation").getString("preferenceform.language.english"));

        switch (OptionsProperties.language) {
            case RUSSIAN:
                language.getSelectionModel().select(0);
                break;
            case ENGLISH:
                language.getSelectionModel().select(1);
                break;
        }

        // Навешиваем события на изменения языка
        language.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                switch (language.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        OptionsProperties.language = Language.RUSSIAN;
                        break;
                    case 1:
                        OptionsProperties.language = Language.ENGLISH;
                        break;
                }
            }
        });

        threadCounter.setValue(OptionsProperties.countThread);
        // Навешиваем событие на изменение количества потоков
        threadCounter.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                OptionsProperties.countThread = ((Double) threadCounter.getValue()).intValue();
            }
        });

    }

    public void onUrlButtonAction() {
        if (OptionsProperties.urlSelectorEnabled) {
            OptionsProperties.urlSelectorEnabled = false;
            urlButton.setText(str_off);
            urlButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            urlButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.urlSelectorEnabled = true;
            urlButton.setText(str_on);
            urlButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            urlButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onCssButtonAction() {
        if (OptionsProperties.cssSelectorEnabled) {
            OptionsProperties.cssSelectorEnabled = false;
            cssButton.setText(str_off);
            cssButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            cssButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.cssSelectorEnabled = true;
            cssButton.setText(str_on);
            cssButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            cssButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onImgButtonAction() {
        if (OptionsProperties.imgSelectorEnabled) {
            OptionsProperties.imgSelectorEnabled = false;
            imgButton.setText(str_off);
            imgButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            imgButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.imgSelectorEnabled = true;
            imgButton.setText(str_on);
            imgButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            imgButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onJSButtonAction() {
        if (OptionsProperties.jsSelectorEnabled) {
            OptionsProperties.jsSelectorEnabled = false;
            jsButton.setText(str_off);
            jsButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            jsButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.jsSelectorEnabled = true;
            jsButton.setText(str_on);
            jsButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            jsButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onExcludeButtonAction() {
        if (OptionsProperties.excludeRepeatedUrl) {
            OptionsProperties.excludeRepeatedUrl = false;
            excludeButton.setText(str_off);
            excludeButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            excludeButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.excludeRepeatedUrl = true;
            excludeButton.setText(str_on);
            excludeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            excludeButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onMovingBeyondButton() {
        if (OptionsProperties.movingBeyond) {
            OptionsProperties.movingBeyond = false;
            mBeyondButton.setText(str_off);
            mBeyondButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            mBeyondButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.movingBeyond = true;
            mBeyondButton.setText(str_on);
            mBeyondButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            mBeyondButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }

    public void onTimeoutKeyEvent() {
        OptionsProperties.timeout = Integer.parseInt(timeoutTF.getText());
    }

    public void onBeyondSubDomainButton() {
        if (OptionsProperties.movingSubDomain) {
            OptionsProperties.movingBeyond = false;
            mBeyondSubDomainButton.setText(str_off);
            mBeyondSubDomainButton.setStyle("-fx-background-color: red;-fx-text-fill:white;");
            mBeyondSubDomainButton.setContentDisplay(ContentDisplay.LEFT);
        } else {
            OptionsProperties.movingSubDomain = true;
            mBeyondSubDomainButton.setText(str_on);
            mBeyondSubDomainButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            mBeyondSubDomainButton.setContentDisplay(ContentDisplay.RIGHT);
        }
    }
}
