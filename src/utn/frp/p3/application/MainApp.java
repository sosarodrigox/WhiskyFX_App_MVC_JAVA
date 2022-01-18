package utn.frp.p3.application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utn.frp.p3.application.view.RootLayoutController;
import utn.frp.p3.application.view.WhiskyController;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private RootLayoutController mainController;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Whisky Manager");
			initRootLayout();
			showWhiskyOverview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			this.rootLayout = (BorderPane) loader.load();
			this.mainController = loader.getController();
			Scene scene = new Scene(rootLayout, 600, 400);
			scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
			this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					System.out.println("Stage is closing");
					System.exit(0);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showWhiskyOverview() {
		try {
			// Load Whisky Overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Whisky.fxml"));
			AnchorPane whiskyOverview = (AnchorPane) loader.load();
			// Set person overview into the center of root layout.
			this.rootLayout.setCenter(whiskyOverview);
			WhiskyController controller = (WhiskyController) loader.getController();
			controller.setPrimarySatge(primaryStage);
			controller.initializedWhiskyController();
			this.mainController.setWhiskyController(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
// TODO Auto-generated method stub
		super.stop();
		Platform.exit();
	}
}