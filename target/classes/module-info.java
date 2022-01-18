module WhiskyFX {
	exports utn.frp.p3.application;
	exports utn.frp.p3.application.model;
	exports utn.frp.p3.application.middleware;
	exports utn.frp.p3.application.view;

	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires vertx.core;
	requires vertx.web.client;
	
	opens utn.frp.p3.application.view to javafx.graphics, javafx.fxml;
}