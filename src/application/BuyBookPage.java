package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import model.Cart;

public class BuyBookPage {
	public VBox BuyBookForm;

	public static Window buyBookWindow;
	
	public static TableView<Cart> CartView;
	public static TableView<Book> marketView;
	TableColumn<Book, Integer> column1;
	TableColumn<Book, Integer> column2;
	TableColumn<Book, Integer> column3;
	TableColumn<Book, Integer> column4;
	TableColumn<Book, Integer> column5;
	TableColumn<Book, Integer> column6;
	Text txtTitle, txtTitle2;
	TableColumn<Cart, Integer> detailColumn1;
	TableColumn<Cart, String> detailColumn2;
	TableColumn<Cart, String> detailColumn3;
	TableColumn<Cart, Integer> detailColumn4;
	TableColumn<Cart, Integer> detailColumn5;
	VBox vbox;
	Label lblQty;
	Spinner<Integer> qtyInput;
	Button btnAddToCart, btnUpdateCart, btnRemoveFromCart, btnCheckout;
	HBox hbox;
	
	public BuyBookPage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public void initComp() {
		CartView = new TableView<>();
		marketView = new TableView<>();
		
		BuyBookForm = new VBox();
		buyBookWindow = new Window();
		
		column1 = new TableColumn<>("ID");		
		column2 = new TableColumn<>("Name");
		column3 = new TableColumn<>("Author");
		column4 = new TableColumn<>("Genre");
		column5 = new TableColumn<>("Stock");
		column6 = new TableColumn<>("Price");
	
		txtTitle = new Text("Book List");

		detailColumn1 = new TableColumn<>("Book ID");
		detailColumn2 = new TableColumn<>("Name");
		detailColumn3 = new TableColumn<>("Author");
		detailColumn4 = new TableColumn<>("Price");
		detailColumn5 = new TableColumn<>("Qty");

		txtTitle2 = new Text("My Cart");

		vbox = new VBox();
		lblQty = new Label("Quantity");
	
		qtyInput = new Spinner<Integer>(1, 99, 1);
	
		btnAddToCart = new Button("Add to Cart");
		btnUpdateCart = new Button("Update Cart");
		btnRemoveFromCart = new Button("Remove from Cart");
		btnCheckout = new Button("Checkout");
	
		hbox = new HBox();
	}
	
	private void addComp() {
		marketView.getColumns().add(column1);
		marketView.getColumns().add(column2);
		marketView.getColumns().add(column3);
		marketView.getColumns().add(column4);
		marketView.getColumns().add(column5);
		marketView.getColumns().add(column6);
		
		BuyBookForm.getChildren().add(txtTitle);
		BuyBookForm.getChildren().add(marketView);
		
		CartView.getColumns().add(detailColumn1);
		CartView.getColumns().add(detailColumn2);
		CartView.getColumns().add(detailColumn3);
		CartView.getColumns().add(detailColumn4);
		CartView.getColumns().add(detailColumn5);
		
		BuyBookForm.getChildren().add(txtTitle2);
		vbox.getChildren().add(lblQty);
		vbox.getChildren().add(qtyInput);
		
		vbox.getChildren().add(btnUpdateCart);
		vbox.getChildren().add(btnAddToCart);
		vbox.getChildren().add(btnRemoveFromCart);
		vbox.getChildren().add(btnCheckout);

		hbox.getChildren().add(CartView);
		hbox.getChildren().add(vbox);
		BuyBookForm.getChildren().add(hbox);
	}

	private void arrangeComp() {
		buyBookWindow.setTitle("Buy Book");
		buyBookWindow.setContentPane(BuyBookForm);
	
		marketView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
		column1.setCellValueFactory(new PropertyValueFactory<>("id"));
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));
		column3.setCellValueFactory(new PropertyValueFactory<>("author"));
		column4.setCellValueFactory(new PropertyValueFactory<>("genreName"));
		column5.setCellValueFactory(new PropertyValueFactory<>("stock"));
		column6.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		txtTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
		VBox.setMargin(txtTitle, new Insets(20, 20, 0, 20));
		VBox.setMargin(marketView, new Insets(0, 20, 20, 20));
	
		CartView.setPrefWidth(900);
		CartView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		detailColumn1.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		detailColumn2.setCellValueFactory(new PropertyValueFactory<>("bookName"));
		detailColumn3.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
		detailColumn4.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
		detailColumn5.setCellValueFactory(new PropertyValueFactory<>("qty"));
	
		VBox.setMargin(txtTitle2, new Insets(0, 20, 0, 20));
		txtTitle2.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
		
		qtyInput.setPrefWidth(500);
		qtyInput.setEditable(true);
		
		btnAddToCart.setPrefWidth(500);
		VBox.setMargin(btnAddToCart, new Insets(20, 0, 0, 0));
		
		btnUpdateCart.setPrefWidth(500);
		btnRemoveFromCart.setPrefWidth(500);
		btnCheckout.setPrefWidth(500);
		
		HBox.setMargin(vbox, new Insets(0, 10, 0, 10));
		VBox.setMargin(hbox, new Insets(0, 20, 20, 20));
	}

	private void action() {
		btnAddToCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (marketView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
	
				if (!Validation.checkQuantity(marketView.getSelectionModel().getSelectedItem().getId(),
						qtyInput.getValue())) {
					return;
				}
	
				if (DatabaseConnection.addCart(Main.userId, marketView.getSelectionModel().getSelectedItem().getId(),
						qtyInput.getValue())) {
					DatabaseConnection.getAllCarts(CartView, Main.userId);
					qtyInput.getValueFactory().setValue(1);
				}
			}
		});
		
		btnUpdateCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (CartView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
	
				if (!Validation.checkQuantity(CartView.getSelectionModel().getSelectedItem().getBookId(),
						qtyInput.getValue())) {
					return;
				}
	
				if (DatabaseConnection.updateCart(Main.userId, CartView.getSelectionModel().getSelectedItem().getBookId(),
						qtyInput.getValue())) {
					DatabaseConnection.getAllCarts(CartView, Main.userId);
					qtyInput.getValueFactory().setValue(1);
				}
			}
		});
		
		btnRemoveFromCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (CartView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
	
				if (DatabaseConnection.removeCart(Main.userId, CartView.getSelectionModel().getSelectedItem().getBookId())) {
					DatabaseConnection.getAllCarts(CartView, Main.userId);
					qtyInput.getValueFactory().setValue(1);
				}
			}
		});
		
		btnCheckout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (CartView.getItems().size() == 0) {
					AlertWindow.show(AlertType.ERROR, "Cart is empty!");
					return;
				}
	
				if (DatabaseConnection.checkoutCart(Main.userId)) {
					AlertWindow.show(AlertType.INFORMATION, "Checkout success!");
					qtyInput.getValueFactory().setValue(1);
					DatabaseConnection.getAllCarts(CartView, Main.userId);
				}
	
			}
		});
	}
}