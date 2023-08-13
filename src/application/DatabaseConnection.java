package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import model.Book;
import model.Cart;
import model.Genre;
import model.Transaction;
import model.TransactionDetail;

public class DatabaseConnection {

	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static private final String DB_URL = "jdbc:mysql://localhost/book_store";
	static private final String USER = "root";
	static private final String PASS = "";

	private static DatabaseConnection instance;
	static private Connection conn = null;
	static private Statement stmt;
	static private ResultSet rs;

	private DatabaseConnection() {
		try {
			// register driver yang akan dipakai
			Class.forName(JDBC_DRIVER);
			// buat koneksi ke database
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql = "SELECT * FROM users";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// System.out.println("email: " + rs.getString("UserEmail"));
				// System.out.println("password: " + rs.getString("UserPass"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Database connection failed");
			alert.show();
		}

	}

	public Connection getConnection() {
		return conn;
	}

	public static DatabaseConnection getInstance() throws SQLException {
		if (instance == null) {
			instance = new DatabaseConnection();
		} else if (instance.getConnection().isClosed()) {
			instance = new DatabaseConnection();
		}

		return instance;
	}

	public static Boolean validEmail(String email) {
		String sql = "SELECT * FROM users WHERE UserEmail='" + email + "'";

		try {
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static int getBookIncrement() {
		try {
			String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'book_store' AND TABLE_NAME = 'book'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("AUTO_INCREMENT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void getAllCarts(TableView<Cart> tableView, int userId) {
		try {
			tableView.getItems().clear();

			String sql = "SELECT * FROM `cart` as c LEFT JOIN book as bk ON c.BookID = bk.BookID WHERE c.UserID = "
					+ userId;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableView.getItems().add(new Cart(rs.getInt("BookID"), rs.getString("BookName"),
						rs.getString("BookAuthor"), rs.getInt("BookPrice"), rs.getInt("CartQty")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getAllBooks(TableView<Book> tableView) {
		try {
			tableView.getItems().clear();

			String sql = "SELECT * FROM `book` as bk LEFT JOIN genre as gr ON bk.GenreID = gr.GenreID";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableView.getItems()
						.add(new Book(rs.getInt("BookID"), rs.getString("BookName"), rs.getString("BookAuthor"),
								rs.getInt("GenreID"), rs.getString("GenreName"), rs.getInt("BookStock"),
								rs.getInt("BookPrice")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getAllGenres(ComboBox<Genre> comboBox) {
		try {
			ObservableList<Genre> data = FXCollections.observableArrayList();

			String sql = "SELECT * FROM `genre`";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				data.add(new Genre(rs.getInt("GenreId"), rs.getString("GenreName")));
			}

			comboBox.setItems(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getAllGenres(TableView<Genre> tableView) {
		try {
			tableView.getItems().clear();

			String sql = "SELECT * FROM `genre`";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableView.getItems().add(new Genre(rs.getInt("GenreId"), rs.getString("GenreName")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getAllTransaction(TableView<Transaction> tableView) {
		try {
			tableView.getItems().clear();

			String sql = "SELECT * FROM `headerTransaction`";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableView.getItems().add(new Transaction(rs.getInt("TransactionID"), rs.getString("TransactionDate")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getAllDetailTransaction(TableView<TransactionDetail> tableView, int transactionId) {
		try {
			tableView.getItems().clear();

			String sql = "SELECT * FROM `detailtransaction` as dt JOIN `book` as bk ON dt.BookId = bk.BookId WHERE dt.TransactionID = %d";
			sql = String.format(sql, transactionId);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableView.getItems().add(new TransactionDetail(rs.getInt("BookID"), rs.getString("BookName"),
						rs.getString("BookAuthor"), rs.getInt("BookPrice"), rs.getInt("TransactionQty")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getQuantity(int id) {
		try {
			String sql = "SELECT * FROM `book` WHERE BookID = %d";
			sql = String.format(sql, id);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("BookStock");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static Boolean checkoutCart(int userId) {
		try {
			String sql = "INSERT INTO headertransaction (UserID, TransactionDate) VALUES (%d, current_timestamp)";
			sql = String.format(sql, userId);
			int count = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (count > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int transactionId = rs.getInt(1);

					String sql2 = "INSERT INTO detailtransaction (TransactionID, BookID, TransactionQty) SELECT '%d',BookID, CartQty FROM cart";
					sql2 = String.format(sql2, transactionId);
					int count2 = stmt.executeUpdate(sql2, transactionId);
					if (count2 > 0) {
						String sql3 = "UPDATE book join cart ON book.BookID=cart.BookID SET book.BookStock=book.BookStock-cart.CartQty WHERE cart.UserID=%d";
						sql3 = String.format(sql3, userId);
						int count3 = stmt.executeUpdate(sql3);
						if (count3 > 0) {
							String sql4 = "DELETE FROM cart WHERE UserID = %d";
							sql4 = String.format(sql4, userId);
							int count4 = stmt.executeUpdate(sql4);
							return count4 > 0;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean addCart(int userId, int bookId, int cartQty) {
		try {
			String sql = "INSERT INTO cart (UserID, BookID, CartQty) VALUE('%d','%d','%d')";
			sql = String.format(sql, userId, bookId, cartQty);
			int count = stmt.executeUpdate(sql);
			return count > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean updateCart(int userId, int bookId, int qty) {
		try {
			String sql = "UPDATE cart SET CartQty = %d WHERE UserId = %d AND BookId = %d";
			sql = String.format(sql, qty, userId, bookId);
			int count = stmt.executeUpdate(sql);
			return count > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean removeCart(int userId, int bookId) {
		try {
			String sql = "DELETE FROM cart WHERE UserId = %d AND BookId = %d";
			sql = String.format(sql, userId, bookId);
			int count = stmt.executeUpdate(sql);
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Boolean insertBook(String name, String author, int genreId, int stock, int price) {
		try {
			String sql = "INSERT INTO book (GenreID, BookName, BookAuthor, BookPrice, BookStock) VALUE('%d', '%s', '%s', '%s', '%s')";
			sql = String.format(sql, genreId, name, author, price, stock);
			int count = stmt.executeUpdate(sql);
			return count > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean updateBook(int id, String name, String author, int genreId, int stock, int price) {
		try {
			String sql = "UPDATE book SET BookName = '%s', BookAuthor = '%s', GenreID = %d, BookStock = %d, BookPrice = %d  WHERE BookId = %d";
			sql = String.format(sql, name, author, genreId, stock, price, id);
			int count = stmt.executeUpdate(sql);
			return count > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean deleteBook(int id) {
		try {
			String sql = "DELETE FROM book WHERE BookId = %d";
			sql = String.format(sql, id);
			int count = stmt.executeUpdate(sql);
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Boolean insertGenre(String name) {
		try {
			String sql = "INSERT INTO genre (GenreName) VALUE('%s')";
			sql = String.format(sql, name);
			int count = stmt.executeUpdate(sql);
			return count > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean registerUser(String fullName, String email, String password, String address, String dob,
			String gender) {
		try {
			String sql = "INSERT INTO users (UserFullName, UserEmail, UserPass, UserAddress, UserDOB, UserGender, UserRole) VALUE('%s', '%s', '%s', '%s', '%s', '%s', 'user')";
			sql = String.format(sql, fullName, email, password, address, dob, gender);
			int count = stmt.executeUpdate(sql);
			return count > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Boolean loginUser(String email, String password) {
		String sql = "SELECT * FROM users WHERE UserEmail='%s' AND UserPass='%s'";
		sql = String.format(sql, email, password);
		try {
			try (ResultSet rs = stmt.executeQuery(sql)) {
				if (rs.next()) {
					Main.userId = rs.getInt("UserID");
					Main.userFullName = rs.getString("UserFullName");
					Main.role = rs.getString("UserRole");
					return true;
				}
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
				AlertWindow.show(AlertType.ERROR, "Failed to execute login query!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertWindow.show(AlertType.ERROR, "An error occurred!");
			return false;
		}
		return false;
	}

}
