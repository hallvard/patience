package patience.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PatienceApp extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {
		String fxmlName = getClass().getSimpleName();
		final int pos = fxmlName.indexOf("App");
		if (pos > 0) {
			fxmlName = fxmlName.substring(0, pos);
		}
		final Parent parent = FXMLLoader.load(getClass().getResource(fxmlName + ".fxml"));
		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
