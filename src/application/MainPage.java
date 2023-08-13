package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;

public class MainPage {
	public GridPane MainForm;

	public static MenuBar mb;
	public static Menu menuManage;
	public static Menu menuTransaction;
	
	BackgroundImage myBI;
	Menu menuUser;
	MenuItem menuItemLogout, menuItemBuy, menuItemBook, menuItemHistory, menuItemGenre;
	
	public MainPage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public void initComp() {
		mb = new MenuBar();
		menuTransaction = new Menu("Transaction");
		menuManage = new Menu("Manage");
		
		MainForm = new GridPane();

		myBI = new BackgroundImage(new Image("file:src/application/assets/bg.jpg", true), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false));

		menuUser = new Menu("User");
		menuItemLogout = new MenuItem("Logout");
		menuItemBuy = new MenuItem("Buy Book");
		menuItemHistory = new MenuItem("View Transaction History");
		menuItemBook = new MenuItem("Book");
		menuItemGenre = new MenuItem("Genre");
	}

	private void addComp() {
		menuUser.getItems().add(menuItemLogout);
		menuTransaction.getItems().add(menuItemBuy);
		menuTransaction.getItems().add(menuItemHistory);
		menuManage.getItems().add(menuItemBook);
		menuManage.getItems().add(menuItemGenre);
		mb.getMenus().add(menuUser);
		mb.getMenus().add(menuTransaction);
	}

	private void arrangeComp() {
		MainForm.setBackground(new Background(myBI));
		Main.rootMain.setTop(mb);
	}

	private void action() {
		menuItemLogout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.currentStage.setMaximized(false);
				Main.root.setCenter(Main.loginPage.loginForm);
				Main.currentStage.setScene(Main.scene);
				Main.rootMain.setCenter(MainForm);
			}
		});

		menuItemBuy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllBooks(BuyBookPage.marketView);
				DatabaseConnection.getAllCarts(BuyBookPage.CartView, Main.userId);
				Main.rootMain.setCenter(BuyBookPage.buyBookWindow);
			}
		});

		menuItemHistory.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllTransaction(HistoryPage.historyView);
				Main.rootMain.setCenter(HistoryPage.historyWindow);
			}
		});

		menuItemBook.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllGenres(BookPage.genre);
				DatabaseConnection.getAllBooks(BookPage.bookView);

				Main.rootMain.setCenter(BookPage.bookWindow);
			}
		});

		menuItemGenre.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllGenres(GenrePage.genreView);
				Main.rootMain.setCenter(GenrePage.genreWindow);
			}
		});
	}

}