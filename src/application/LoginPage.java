package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginPage {
	public GridPane loginForm;
	
	Text txtTitle;
	Label lblEmail, lblPassword, lblRegister;
	TextField emailInput;
	PasswordField passwordInput;
	Button btnLogin;
	Hyperlink linkRegister;

	public LoginPage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public void initComp() {
		loginForm = new GridPane();
		txtTitle = new Text("Login");
		lblEmail = new Label("Email");
		emailInput = new TextField();
		lblPassword = new Label("Password");
		passwordInput = new PasswordField();
		btnLogin = new Button("Login");
		lblRegister = new Label("Don't have an account?");
		linkRegister = new Hyperlink("Register");
	}

	private void addComp() {
		loginForm.getStyleClass().add("bg-white");		

		loginForm.add(txtTitle, 0, 1, 2, 1);		
		loginForm.add(lblEmail, 0, 2);
		loginForm.add(emailInput, 1, 2);		
		loginForm.add(lblPassword, 0, 3);		
		loginForm.add(passwordInput, 1, 3);		
		btnLogin.getStyleClass().add("btn");
		loginForm.add(btnLogin, 1, 4, 1, 1);		
		loginForm.add(lblRegister, 0, 5, 2, 1);
		loginForm.add(linkRegister, 0, 6, 2, 1);
	}

	private void arrangeComp() {
		loginForm.setAlignment(Pos.CENTER);
		loginForm.setHgap(20);
		loginForm.setVgap(10);
		
		txtTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
		GridPane.setMargin(txtTitle, new Insets(0, 0, 20, 0));
		GridPane.setHalignment(txtTitle, HPos.CENTER);
		
		emailInput.setPromptText("Email");
		emailInput.setPrefWidth(225);
		
		passwordInput.setPromptText("Password");
			
		btnLogin.setPrefWidth(225);		
		GridPane.setMargin(btnLogin, new Insets(0, 0, 20, 0));
		
		GridPane.setHalignment(lblRegister, HPos.CENTER);
		
		GridPane.setHalignment(linkRegister, HPos.CENTER);
	}

	private void action() {
		btnLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!Validation.validateLogin(emailInput.getText(), passwordInput.getText())) {
					return;
				}

				if (DatabaseConnection.loginUser(emailInput.getText(), passwordInput.getText())) {
					emailInput.clear();
					passwordInput.clear();

					if (Main.role.equals("admin")) {
						MainPage.mb.getMenus().remove(1);
						MainPage.mb.getMenus().add(MainPage.menuManage);
					} else {
						MainPage.mb.getMenus().remove(1);
						MainPage.mb.getMenus().add(MainPage.menuTransaction);
					}

					AlertWindow.show(AlertType.INFORMATION, "Welcome, " + Main.userFullName + "!");

					Main.currentStage.setScene(Main.sceneMain);
				} else {
					AlertWindow.show(AlertType.ERROR, "Invalid username or password");
				}
			}
		});
		
		linkRegister.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.root.setCenter(RegisterPage.getInstance());
			}
		});
	}
}