package no.hal.patience.fx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public abstract class PatienceApp extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        loadAndSetScene(primaryStage);
        primaryStage.show();
    }

    private void loadAndSetScene(Stage stage) {
        String fxmlName = getClass().getSimpleName();
        final int pos = fxmlName.indexOf("App");
        if (pos > 0) {
            fxmlName = fxmlName.substring(0, pos);
        }
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource(fxmlName + ".fxml"));
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loadAndSetScene(stage);
            }
        });
		stage.setScene(scene);
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
