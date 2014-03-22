package vstu.gui.forms.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import vstu.gui.data.Options;

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
        Options.load();

        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
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

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
