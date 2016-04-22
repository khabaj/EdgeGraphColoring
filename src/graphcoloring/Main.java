package graphcoloring;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/Layout.fxml"));
			BorderPane rootElement = (BorderPane) loader.load();
			Scene scene = new Scene(rootElement, 1000, 600);

			primaryStage.setTitle("Edge Graph Coloring");
			primaryStage.setMinWidth(1000);
			primaryStage.setMinHeight(600);
			primaryStage.setScene(scene);
			
			EdgeGraphColoring controller = (EdgeGraphColoring)loader.getController();
			controller.setStage(primaryStage);
			
			primaryStage.show();		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);	
	}
}
