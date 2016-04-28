package graphcoloring;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/Layout.fxml"));
			BorderPane rootElement = (BorderPane) loader.load();
			Scene scene = new Scene(rootElement, 1000, 650);
			
			primaryStage.setTitle("Edge Graph Coloring");
			primaryStage.setMinWidth(1000);
			primaryStage.setMinHeight(650);
			primaryStage.setScene(scene);

			GraphColoringController controller = (GraphColoringController) loader.getController();
			controller.setStage(primaryStage);
			primaryStage.show();

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent event) {
					Platform.exit();
					System.exit(0);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
