package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	public static Stage currentStage;
	public static Scene scene;
	public static Scene sceneMain;
	public static BorderPane root;
	public static BorderPane rootMain;
	public static LoginPage loginPage;
	public static RegisterPage registerPage;
	public static MainPage mainPage;
	BuyBookPage buyBookPage;
	HistoryPage historyPage;
	BookPage bookPage;
	GenrePage genrePage;

	public static int userId = 0;
	public static String userFullName = null;
	public static String role = "user";

	public void init() {
		root = new BorderPane();
		scene = new Scene(root, 400, 520);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		rootMain = new BorderPane();
		sceneMain = new Scene(rootMain, 1024, 650);
		 
		loginPage = new LoginPage();
		registerPage = new RegisterPage();
		mainPage = new MainPage();
		buyBookPage = new BuyBookPage();
		historyPage = new HistoryPage();
		bookPage = new BookPage();
		genrePage = new GenrePage();
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			currentStage = primaryStage;

			init();

			root.setCenter(loginPage.loginForm);
			rootMain.setCenter(mainPage.MainForm);
			primaryStage.getIcons().add(new Image("file:src/application/assets/icon.png"));
			primaryStage.setTitle("Bookstore");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();

			DatabaseConnection.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
