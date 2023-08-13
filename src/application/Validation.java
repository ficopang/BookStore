package application;

import javafx.scene.control.Alert.AlertType;

public class Validation {

	public static Boolean isAlphaNumeric(String str) {
		Boolean isAlpha = false;
		Boolean isNumeric = false;

		for (int i = 0, len = str.length(); i < len; i++) {
			char chr = str.charAt(i);
			if (chr > 47 && chr < 58) // numeric (0-9)
				isNumeric = true;
			if (chr > 64 && chr < 91) // upper alpha (A-Z)
				isAlpha = true;
			if (chr > 96 && chr < 123) // lower alpha (a-z))
				isAlpha = true;
		}

		if (!isAlpha || !isNumeric)
			return false;

		return true;
	};

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static Boolean validateLogin(String email, String password) {
		if (email.isEmpty()) {
			AlertWindow.show(AlertType.ERROR, "Email is empty!");
			return false;
		}

		if (password.isEmpty()) {
			AlertWindow.show(AlertType.ERROR, "Password is empty!");
			return false;
		}

		return true;
	}

	public static Boolean validateRegister(String fullName, String email, String pass, String cfr_pass, String address,
			String DOB, String gender, Boolean tnc) {

		if (!isValidFullName(fullName)) {
			return false;
		}

		if (!isValidEmail(email)) {
			return false;
		}

		if (!DatabaseConnection.validEmail(email)) {
			AlertWindow.show(AlertType.ERROR, "Email has been taken!");
			return false;
		}

		if (pass.length() < 6 || pass.length() > 20) {
			AlertWindow.show(AlertType.ERROR, "Password must be between 6 - 20 characters!");
			return false;
		}

		if (!isAlphaNumeric(pass)) {
			AlertWindow.show(AlertType.ERROR, "Password must be alphanumeric!");
			return false;
		}

		if (!pass.equals(cfr_pass)) {
			AlertWindow.show(AlertType.ERROR, "Confirm password must equal to password!");
			return false;
		}

		if (address.isEmpty()) {
			AlertWindow.show(AlertType.ERROR, "Address must be filled!");
			return false;
		}

		if (DOB.isEmpty()) {
			AlertWindow.show(AlertType.ERROR, "Date of Birth must be filled!");
			return false;
		}

		if (!tnc) {
			AlertWindow.show(AlertType.ERROR, "You must agree to terms and conditions!");
			return false;
		}

		return true;
	}

	public static boolean isValidEmail(String email) {
		if (email.isEmpty() || email.charAt(0) == '@' || email.charAt(0) == '.'
				|| email.charAt(email.length() - 1) == '@' || email.charAt(email.length() - 1) == '.'
				|| email.charAt(email.indexOf('@') + 1) == '.' || email.chars().filter(ch -> ch == '@').count() < 1
				|| email.chars().filter(ch -> ch == '@').count() > 1
				|| email.substring(email.indexOf('@') + 1, email.length()).chars().filter(ch -> ch == '.')
						.count() < 1) {
			AlertWindow.show(AlertType.ERROR, "Please fill with valid email!");
			return false;
		}
		return true;
	}

	public static boolean isValidFullName(String fullName) {
		if (fullName.length() < 5 || fullName.length() > 30) {
			AlertWindow.show(AlertType.ERROR, "Full name must be between 5 - 30 characters!");
			return false;
		}
		return true;
	}

	public static Boolean validateGenre(String name) {
		if (name.length() < 5 || name.length() > 12) {
			AlertWindow.show(AlertType.ERROR, "Name must be between 5 - 12 characters!");
			return false;
		}

		return true;
	}

	public static Boolean validateBook(String name, String author, String genre, String price, int stock) {
		if (name.length() < 5 || name.length() > 45) {
			AlertWindow.show(AlertType.ERROR, "Name must be between 5 - 45 characters!");
			return false;
		}

		if (author.length() < 5 || author.length() > 30) {
			AlertWindow.show(AlertType.ERROR, "Author must be between 5 - 30 characters!");
			return false;
		}

		if (genre.isEmpty()) {
			AlertWindow.show(AlertType.ERROR, "Genre must be choosed!");
			return false;
		}

		if (!isNumeric(price)) {
			AlertWindow.show(AlertType.ERROR, "Price must be numeric!");
			return false;
		}

		if (stock == 0) {
			AlertWindow.show(AlertType.ERROR, "Stock must more than 0!");
			return false;
		}

		return true;
	}

	public static boolean checkQuantity(int id, int qty) {

		if (DatabaseConnection.getQuantity(id) < qty) {
			AlertWindow.show(AlertType.ERROR, "Quantity must not be higher than stock!");
			return false;
		}

		return true;
	}

}
