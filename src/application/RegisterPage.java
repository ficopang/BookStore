package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class RegisterPage {
	public static GridPane registerForm;
	
	Text txtTitle;
	Label lblFullName, lblEmail, lblPassword, lblCfrPassword, lblAddress, lblDOB, lblGender, lblRegister;
	TextField fullnameInput, emailInput, addressInput;
	PasswordField passwordInput, cfrpasswordInput;
	DatePicker DOBInput;
	RadioButton maleOption, femaleOption;
	ToggleGroup gender;
	CheckBox tnc;
	Button btnRegister;
	Hyperlink linkLogin;

	public RegisterPage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public static GridPane getInstance() {
		if(registerForm == null) {
			registerForm = new GridPane();
		}
		
		return registerForm;
	}

	public void initComp() {
		registerForm = new GridPane();
	
		txtTitle = new Text("Register");	
		lblFullName = new Label("Full Name");	
		fullnameInput = new TextField();	
		lblEmail = new Label("Email");	
		emailInput = new TextField();	
		lblPassword = new Label("Password");
		passwordInput = new PasswordField();
		lblCfrPassword = new Label("Confirm Password");	
		cfrpasswordInput = new PasswordField();	
		lblAddress = new Label("Address");	
		addressInput = new TextField();	
		lblDOB = new Label("Date of Birth");	
		DOBInput = new DatePicker();
		
		lblGender = new Label("Gender");
		maleOption = new RadioButton("Male");
		femaleOption = new RadioButton("Female");
		gender = new ToggleGroup();
		
		tnc = new CheckBox("Agree to the terms and conditions");
		btnRegister = new Button("Register");	
		lblRegister = new Label("Already have an account?");	
		linkLogin = new Hyperlink("Login");	
	}
	
	private void addComp() {
		registerForm.add(txtTitle, 0, 1, 3, 1);
		registerForm.add(lblFullName, 0, 2);
		registerForm.add(fullnameInput, 1, 2, 2, 1);
		registerForm.add(lblEmail, 0, 3);
		registerForm.add(emailInput, 1, 3, 2, 1);
		registerForm.add(lblPassword, 0, 4);	
		registerForm.add(passwordInput, 1, 4, 2, 1);
		registerForm.add(lblCfrPassword, 0, 5);
		registerForm.add(cfrpasswordInput, 1, 5, 2, 1);
		registerForm.add(lblAddress, 0, 6);
		registerForm.add(addressInput, 1, 6, 2, 1);
		registerForm.add(lblDOB, 0, 7);
		registerForm.add(DOBInput, 1, 7, 2, 1);
		registerForm.add(lblGender, 0, 8);
		registerForm.add(maleOption, 1, 8);
		registerForm.add(femaleOption, 2, 8);
		registerForm.add(tnc, 1, 9, 2, 1);
		btnRegister.getStyleClass().add("btn");
		registerForm.add(btnRegister, 1, 10, 2, 1);
		registerForm.add(lblRegister, 0, 11, 3, 1);
		registerForm.add(linkLogin, 0, 12, 3, 1);
	}

	private void arrangeComp() {
		registerForm.getStyleClass().add("bg-white");
		registerForm.setAlignment(Pos.CENTER);
		registerForm.setHgap(20);
		registerForm.setVgap(10);
		
		txtTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
		GridPane.setHalignment(txtTitle, HPos.CENTER);
		GridPane.setMargin(txtTitle, new Insets(0, 0, 20, 0));
		
		fullnameInput.setPromptText("Full Name");		
		emailInput.setPromptText("Email");		
		passwordInput.setPromptText("Password");		
		cfrpasswordInput.setPromptText("Confirm Password");
		addressInput.setPromptText("Address");
		
		DOBInput.setPrefWidth(Double.MAX_VALUE);
		
		maleOption.setToggleGroup(gender);
		femaleOption.setToggleGroup(gender);
		maleOption.setPrefWidth(100);
		femaleOption.setPrefWidth(100);
		maleOption.setSelected(true);
		
		GridPane.setMargin(tnc, new Insets(10, 0, 10, 0));
		
		GridPane.setHalignment(btnRegister, HPos.CENTER);
		btnRegister.setPrefWidth(225);
	
		GridPane.setMargin(btnRegister, new Insets(0, 0, 20, 0));
		GridPane.setHalignment(lblRegister, HPos.CENTER);
		
		GridPane.setHalignment(linkLogin, HPos.CENTER);
	}

	private void action() {
		DOBInput.setConverter(new StringConverter<LocalDate>() {
			String pattern = "yyyy-MM-dd";
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
	
			{
				DOBInput.setPromptText(pattern.toLowerCase());
			}
	
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}
	
			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
		
		btnRegister.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Validation.validateRegister(fullnameInput.getText(), emailInput.getText(), passwordInput.getText(),
						cfrpasswordInput.getText(), addressInput.getText(),
						DOBInput.getConverter().toString(DOBInput.getValue()),
						((RadioButton) gender.getSelectedToggle()).getText(), tnc.isSelected())) {
					if (DatabaseConnection.registerUser(fullnameInput.getText(), emailInput.getText(),
							passwordInput.getText(), addressInput.getText(),
							DOBInput.getConverter().toString(DOBInput.getValue()),
							((RadioButton) gender.getSelectedToggle()).getText())) {
						AlertWindow.show(AlertType.INFORMATION, "Register successfully!");
						fullnameInput.clear();
						emailInput.clear();
						passwordInput.clear();
						cfrpasswordInput.clear();
						addressInput.clear();
						DOBInput.getEditor().clear();
						tnc.setSelected(false);
	
						Main.root.setCenter(Main.loginPage.loginForm);
					} else {
						AlertWindow.show(AlertType.ERROR, "Oops.. Something went wrong. Please try again later!");
					}
				}
			}
		});
		
		linkLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.root.setCenter(Main.loginPage.loginForm);
			}
		});
	}
}