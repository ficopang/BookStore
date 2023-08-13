package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertWindow {

	public AlertWindow() {
	}
	
	public static void show(AlertType alertType, String message) {
		Alert alert = new Alert(alertType);
		String[] titles = {"-", "Notification", "Warning", "Confirm", "Error"};
		String title = titles[alertType.ordinal()];
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:src/application/assets/icon.png"));
		
		alert.showAndWait();
	}

}
