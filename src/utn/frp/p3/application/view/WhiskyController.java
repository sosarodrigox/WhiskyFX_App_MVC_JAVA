package utn.frp.p3.application.view;

import java.io.IOException;
import java.util.Optional;
import io.vertx.core.Future;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utn.frp.p3.application.MainApp;
import utn.frp.p3.application.middleware.ServerAccess;
import utn.frp.p3.application.model.Whisky;

public class WhiskyController {
	/**
	 * The data as an observable list of Persons.
	 */
	private ObservableList<Whisky> whiskyData = FXCollections.observableArrayList();
	@FXML
	private TableView<Whisky> whiskyTable;
	@FXML
	private TableColumn<Whisky, String> nameColumn;
	@FXML
	private TableColumn<Whisky, String> originColumn;
	@FXML
	private Label idLabel;
	@FXML
	private Label nameLabel;
	@FXML
	private Label originLabel;
	private int selectedIndex;
	private Stage primaryStage;
	
	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the whisky table with the two columns.
		this.whiskyTable.setItems(whiskyData);
		this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		this.originColumn.setCellValueFactory(cellData -> cellData.getValue().getOrigin());
		// Clear Whisky details.
		showWhiskyDetails(null);
		// Listen for selection changes and show the Whisky details when changed.
		whiskyTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showWhiskyDetails(newValue));
	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit details
	 * for a new Whisky.
	 */
	@FXML
	public void handleNewWhisky() {
		System.out.println("handleNewWhisky");
		Whisky tempWhisky = new Whisky();
		
		boolean okClicked = this.showWhiskyEditDialog(tempWhisky);
		showWhiskyEditDialog(tempWhisky);
		if (okClicked) {
			this.whiskyData.add(tempWhisky);
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit details
	 * for the selected Whisky.
	 */
	@FXML
	public void handleEditWhisky() {
		System.out.println("handleEditWhisky");
		this.selectedIndex = whiskyTable.getSelectionModel().getSelectedIndex();
		Whisky selectedWhisky = whiskyTable.getSelectionModel().getSelectedItem();
		if (selectedWhisky != null) {
			boolean okClicked = this.showWhiskyEditDialog(selectedWhisky);
			if (okClicked) {
				showWhiskyDetails(selectedWhisky);
			}
		} else {
			// Nothing selected.
			Alert exceptionDialog = new Alert(AlertType.WARNING);
			exceptionDialog.setTitle("Sin selección");
			exceptionDialog.setHeaderText("No hay selección");
			exceptionDialog.setContentText("Seleccione un Whisky de la tabla");
			exceptionDialog.show();
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	public void handleDeleteWhisky() {
		this.selectedIndex = whiskyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Whisky whisky = whiskyTable.getItems().get(selectedIndex);
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmación de usuario");
			String s = "¿Desea borrar este item: " + whisky.getId().get() + " - " + whisky.getName().getValue() + "?";
			alert.setContentText(s);
			Optional<ButtonType> result = alert.showAndWait();
			if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
				Future<Void> deleteResult = ServerAccess.deleteWhisky(whisky);
				deleteResult.setHandler(ar -> {
					if (ar.succeeded()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								// if you change the UI, do it here!
								whiskyTable.getSelectionModel().clearSelection();
								whiskyData.remove(selectedIndex);
							}
						});
					} else {
						System.out.println(ar.cause());
					}
				});
			}
		} else {
			// Nothing selected.
			Alert exceptionDialog = new Alert(AlertType.WARNING);
			exceptionDialog.setTitle("Sin selección");
			exceptionDialog.setHeaderText("No hay selección");
			exceptionDialog.setContentText("Seleccione un Whisky de la tabla");
			exceptionDialog.show();
		}
	}

	public void initializedWhiskyController() {
		ServerAccess.getWhiskyStock(whiskyData);
	}

	private void showWhiskyDetails(Whisky whisky) {
		if (whisky != null) {
			idLabel.setText(whisky.getId().getValue().toString());
			nameLabel.setText(whisky.getName().getValue());
			originLabel.setText(whisky.getOrigin().getValue());
		} else {
			idLabel.setText("-");
			nameLabel.setText("-");
			originLabel.setText("-");
		}
	}

	public void setPrimarySatge(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Opens a dialog to edit details for the specified person. If the user clicks
	 * OK, the changes are saved into the provided person object and true is
	 * returned.
	 *
	 * @param person the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showWhiskyEditDialog(Whisky whisky) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WhiskyEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edición de Whisky");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			// Set the person into the controller.
			WhiskyEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			
			controller.setWhisky(whisky);
			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
