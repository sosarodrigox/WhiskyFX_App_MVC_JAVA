package utn.frp.p3.application.view;

import io.vertx.core.Future;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utn.frp.p3.application.middleware.ServerAccess;
import utn.frp.p3.application.model.Whisky;

public class WhiskyEditDialogController {
	@FXML
	private Label idLabel;
	@FXML
	private TextField nameField;
	@FXML
	private TextField originField;
	private Stage dialogStage;
	private boolean okClicked = false;
	private Whisky whisky;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the person to be edited in the dialog.
	 *
	 * @param person
	 */
	public void setWhisky(Whisky whisky) {
		this.whisky = whisky;
		this.idLabel.setText(whisky.getId().getValue().toString());
		this.nameField.setText(whisky.getName().getValue());
		this.originField.setText(whisky.getOrigin().getValue());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 *
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			Future<Integer> handlerResult = (whisky.getId().get() == 0)
					? ServerAccess.insertWhisky(nameField.getText(), originField.getText())
					: ServerAccess.updateWhisky(whisky.getId().get(), nameField.getText(), originField.getText());
			handlerResult.setHandler(ar -> {
				if (ar.succeeded()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
// if you change the UI, do it here !
							whisky.setId(ar.result().intValue());
							whisky.setName(nameField.getText());
							whisky.setOrigin(originField.getText());
							okClicked = true;
							dialogStage.close();
						}
					});
				} else {
					System.out.println(ar.cause());
					okClicked = false;
					dialogStage.close();
				}
			});
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 *
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		if (nameField.getText() == null || nameField.getText().length() == 0) {
			errorMessage += "¡Marca de whisky no válida!\n";
		}
		if (originField.getText() == null || originField.getText().length() == 0) {
			errorMessage += "¡Origen no válido!\n";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
// Show the error message.
			Alert exceptionDialog = new Alert(AlertType.ERROR);
			exceptionDialog.setTitle("Error de carga de datos");
			exceptionDialog.setHeaderText("Corrija los siguientes errores");
			exceptionDialog.setContentText(errorMessage);
			exceptionDialog.show();
			return false;
		}
	}
}