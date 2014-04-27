package vstu.gui.forms.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.text.resources.FormatData_es_MX;
import vstu.gui.data.OptionsProperties;

import javax.imageio.ImageTranscoder;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Lopatin on 21.03.14.
 */
public class MainForm extends Application {


    private static MainForm instance;

    public MainForm() {
        instance = this;
    }


    @Override
    public void start(Stage stage) throws Exception {
        OptionsProperties.load();
        switch (OptionsProperties.language) {
            case ENGLISH:
                Locale.setDefault(Locale.ENGLISH);
                break;
            case RUSSIAN:
                Locale.setDefault(new Locale.Builder().setLanguage("ru").build());
                break;
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setResources(ResourceBundle.getBundle("translation"));

        Parent root = (Parent) fxmlLoader.load(this.getClass().getResource("MainForm.fxml").openStream());
        stage.setTitle("Java XENU");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Событие на закрытие окна
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        root.getStylesheets().add("/vstu/gui/forms/table.css");
        stage.getIcons().add(new Image("icon.png"));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
