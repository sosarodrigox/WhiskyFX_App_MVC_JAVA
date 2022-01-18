package utn.frp.p3.application.view;

import javafx.fxml.FXML;

public class RootLayoutController {
	WhiskyController wController;

	@FXML
	public void onExit() {
		System.exit(0);
	}

	public void setWhiskyController(WhiskyController wController) {
		this.wController = wController;
	}
}
