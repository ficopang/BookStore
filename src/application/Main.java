package application;
	
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import model.Cart;
import model.Genre;
import model.Transaction;
import model.TransactionDetail;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Main extends Application {
	
	Stage currentStage;
	Scene scene;
	Scene sceneMain;
	Window buyBookWindow;
	Window historyWindow;
	Window bookWindow;
	Window genreWindow;
	BorderPane root;
	BorderPane rootMain;
	GridPane loginForm;
	GridPane registerForm;
	GridPane mainForm;
	VBox BuyBookForm;
	HBox historyForm;
	HBox bookForm;
	HBox genreForm;
    MenuBar mb;
    Menu menuManage;
    Menu menuTransaction;
    ComboBox<Genre> genre;
    TextField idInput;
	
	static int userId = 0;
	static String userFullName = null;
	static String role = "user";
	
    TableView<Cart> CartView = new TableView<>();
    TableView<Book> marketView = new TableView<>();
	TableView<Book> bookView = new TableView<>();
	TableView<Genre> genreView = new TableView<>();
    TableView<Transaction> historyView = new TableView<>();
    TableView<TransactionDetail> detailView = new TableView<>();
	
	public void init() {
		root = new BorderPane();
		scene = new Scene(root,400,520);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		rootMain = new BorderPane();
		sceneMain = new Scene(rootMain,1024,650);
		
		loginForm = new GridPane();
		loginForm.getStyleClass().add("bg-white");
		registerForm = new GridPane();
		registerForm.getStyleClass().add("bg-white");
		mainForm = new GridPane();
		BuyBookForm = new VBox();
		historyForm = new HBox();
		bookForm = new HBox();
		genreForm = new HBox();
		
		buyBookWindow = new Window();
		historyWindow = new Window();
		bookWindow = new Window();
		genreWindow = new Window();
		
	    mb = new MenuBar();
		menuTransaction = new Menu("Transaction");
		menuManage = new Menu("Manage");
	}
	
	public void initLoginForm() {
		loginForm.setAlignment(Pos.CENTER);
		loginForm.setHgap(20);
		loginForm.setVgap(10);
		
	    Text txtTitle = new Text("Login");
	    txtTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
	    GridPane.setMargin(txtTitle, new Insets(0, 0, 20, 0));
	    GridPane.setHalignment(txtTitle, HPos.CENTER);
	    loginForm.add(txtTitle, 0, 1, 2, 1);
	    
	    Label lblEmail = new Label("Email");
        loginForm.add(lblEmail, 0, 2);
	    
        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");
        emailInput.setPrefWidth(225);
        loginForm.add(emailInput, 1, 2);
        
	    Label lblPassword = new Label("Password");
        loginForm.add(lblPassword, 0, 3);
        
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        loginForm.add(passwordInput, 1, 3);
        
        Button btnLogin = new Button("Login");
        // GridPane.setHalignment(btnLogin, HPos.CENTER);
        btnLogin.setPrefWidth(225);
        btnLogin.getStyleClass().add("btn");
        GridPane.setMargin(btnLogin, new Insets(0, 0, 20, 0));
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!Validation.validateLogin(emailInput.getText(), passwordInput.getText())) {
					return;
				}
				
				if(DatabaseConnection.loginUser(emailInput.getText(), passwordInput.getText())) {
					emailInput.clear();
					passwordInput.clear();
					
					if(role.equals("admin")) {
						mb.getMenus().remove(1);
						mb.getMenus().add(menuManage);
					} else {
						mb.getMenus().remove(1);
						mb.getMenus().add(menuTransaction);
					}
					
					AlertWindow.show(AlertType.INFORMATION, "Welcome, " + userFullName + "!");
					
					currentStage.setScene(sceneMain);
				} else {
					AlertWindow.show(AlertType.ERROR, "Invalid username or password");
				}
			}
		});
        loginForm.add(btnLogin, 1, 4, 1, 1);
        
	    Label lblRegister = new Label("Don't have an account?");
	    GridPane.setHalignment(lblRegister, HPos.CENTER);
        loginForm.add(lblRegister, 0, 5, 2, 1);
        
        Hyperlink linkRegister = new Hyperlink("Register");
        GridPane.setHalignment(linkRegister, HPos.CENTER);
        linkRegister.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				root.setCenter(registerForm);
			}
		});
        loginForm.add(linkRegister, 0, 6, 2, 1);
	}
	
	public void initRegisterForm() {
		registerForm.setAlignment(Pos.CENTER);
		registerForm.setHgap(20);
		registerForm.setVgap(10);
		
	    Text txtTitle = new Text("Register");
	    txtTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
	    GridPane.setHalignment(txtTitle, HPos.CENTER);
	    GridPane.setMargin(txtTitle, new Insets(0, 0, 20, 0));
	    registerForm.add(txtTitle, 0, 1, 3, 1);
	    
	    Label lblFullName = new Label("Full Name");
	    registerForm.add(lblFullName, 0, 2);
	    
        TextField fullnameInput = new TextField();
        fullnameInput.setPromptText("Full Name");
        registerForm.add(fullnameInput, 1, 2, 2, 1);
	    
	    Label lblEmail = new Label("Email");
	    registerForm.add(lblEmail, 0, 3);
	    
        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");
        registerForm.add(emailInput, 1, 3, 2, 1);
        
	    Label lblPassword = new Label("Password");
	    registerForm.add(lblPassword, 0, 4);
        
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        registerForm.add(passwordInput, 1, 4, 2, 1);
        
	    Label lblCfrPassword = new Label("Confirm Password");
	    registerForm.add(lblCfrPassword, 0, 5);
        
        PasswordField cfrpasswordInput = new PasswordField();
        cfrpasswordInput.setPromptText("Confirm Password");
        registerForm.add(cfrpasswordInput, 1, 5, 2, 1);
        
	    Label lblAddress = new Label("Address");
	    registerForm.add(lblAddress, 0, 6);
        
	    TextField addressInput = new TextField();
        addressInput.setPromptText("Address");
        registerForm.add(addressInput, 1, 6, 2, 1);
        
	    Label lblDOB = new Label("Date of Birth");
	    registerForm.add(lblDOB, 0, 7);
	    
        DatePicker DOBInput = new DatePicker();
        DOBInput.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
            	DOBInput.setPromptText(pattern.toLowerCase());
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        DOBInput.setPrefWidth(Double.MAX_VALUE);
        registerForm.add(DOBInput, 1, 7, 2, 1);
        
	    Label lblGender = new Label("Gender");
	    registerForm.add(lblGender, 0, 8);
	    
	    RadioButton maleOption = new RadioButton("Male");
	    RadioButton femaleOption = new RadioButton("Female");
	    ToggleGroup gender = new ToggleGroup();
	    maleOption.setToggleGroup(gender);
	    femaleOption.setToggleGroup(gender);
        maleOption.setPrefWidth(100);
        femaleOption.setPrefWidth(100);
        maleOption.setSelected(true);
	    registerForm.add(maleOption, 1, 8);
	    registerForm.add(femaleOption, 2, 8);
        
	    CheckBox tnc = new CheckBox("Agree to the terms and conditions");
        GridPane.setMargin(tnc, new Insets(10, 0, 10, 0));
	    //GridPane.setHalignment(tnc, HPos.CENTER);
	    registerForm.add(tnc, 1, 9, 2, 1);
	    
        Button btnRegister = new Button("Register");
        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(Validation.validateRegister(
						fullnameInput.getText(),
						emailInput.getText(),
						passwordInput.getText(),
						cfrpasswordInput.getText(),
						addressInput.getText(),
						DOBInput.getConverter().toString(DOBInput.getValue()),
						((RadioButton)gender.getSelectedToggle()).getText(),
						tnc.isSelected()
				)) {
					if(DatabaseConnection.registerUser(fullnameInput.getText(), emailInput.getText(), passwordInput.getText(), addressInput.getText(), DOBInput.getConverter().toString(DOBInput.getValue()), ((RadioButton)gender.getSelectedToggle()).getText())) {
						AlertWindow.show(AlertType.INFORMATION, "Register successfully!");
						fullnameInput.clear();
						emailInput.clear();
						passwordInput.clear();
						cfrpasswordInput.clear();
						addressInput.clear();
						DOBInput.getEditor().clear();
						tnc.setSelected(false);
						
						root.setCenter(loginForm);
					} else {
						AlertWindow.show(AlertType.ERROR, "Oops.. Something went wrong. Please try again later!");
					}
				}
			}
		});
        GridPane.setHalignment(btnRegister, HPos.CENTER);
        btnRegister.setPrefWidth(225);
        btnRegister.getStyleClass().add("btn");
        GridPane.setMargin(btnRegister, new Insets(0, 0, 20, 0));
        registerForm.add(btnRegister, 1, 10, 2, 1);
        
	    Label lblRegister = new Label("Already have an account?");
	    GridPane.setHalignment(lblRegister, HPos.CENTER);
	    registerForm.add(lblRegister, 0, 11, 3, 1);
        
        Hyperlink linkLogin = new Hyperlink("Login");
        GridPane.setHalignment(linkLogin, HPos.CENTER);
        linkLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				root.setCenter(loginForm);
			}
		});
        registerForm.add(linkLogin, 0, 12, 3, 1);
        
	}
	
	public void initMainForm() {
	    BackgroundImage myBI = new BackgroundImage(
	    		new Image("file:src/application/assets/bg.jpg", true),
	    		BackgroundRepeat.NO_REPEAT,
	            BackgroundRepeat.NO_REPEAT,
	            BackgroundPosition.DEFAULT,
	            new BackgroundSize(1.0, 1.0, true, true, false, false));
	    mainForm.setBackground(new Background(myBI));
	    
	    Menu menuUser = new Menu("User");
	    MenuItem menuItemLogout = new MenuItem("Logout");
	    menuItemLogout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentStage.setMaximized(false);
				root.setCenter(loginForm);
			    currentStage.setScene(scene);
			    rootMain.setCenter(mainForm);
			}
		});
	    menuUser.getItems().add(menuItemLogout);
	    
	    MenuItem menuItemBuy = new MenuItem("Buy Book");
	    menuItemBuy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllBooks(marketView);
				DatabaseConnection.getAllCarts(CartView, userId);
				rootMain.setCenter(buyBookWindow);
			}
		});
	    menuTransaction.getItems().add(menuItemBuy);
	    MenuItem menuItemHistory = new MenuItem("View Transaction History");
	    menuItemHistory.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllTransaction(historyView);
				rootMain.setCenter(historyWindow);
			}
		});
	    menuTransaction.getItems().add(menuItemHistory);
	    
	    MenuItem menuItemBook = new MenuItem("Book");
	    menuItemBook.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllGenres(genre);
			    DatabaseConnection.getAllBooks(bookView);
			    
				//idInput.setText(String.valueOf(DatabaseConnection.getBookIncrement()));
				rootMain.setCenter(bookWindow);
			}
		});
	    menuManage.getItems().add(menuItemBook);
	    
	    MenuItem menuItemGenre = new MenuItem("Genre");
	    menuItemGenre.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DatabaseConnection.getAllGenres(genreView);
				rootMain.setCenter(genreWindow);
			}
		});
	    menuManage.getItems().add(menuItemGenre);
	    
	    mb.getMenus().add(menuUser);
	    mb.getMenus().add(menuTransaction);
	    
	    rootMain.setTop(mb);
	}
	
	public void initBuyBookForm() {
		buyBookWindow.setTitle("Buy Book");
		buyBookWindow.setContentPane(BuyBookForm);
		
	    marketView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn<Book, Integer> column1 = new TableColumn<>("ID");
	    column1.setCellValueFactory(new PropertyValueFactory<>("id"));
	    TableColumn<Book, String> column2 = new TableColumn<>("Name");
	    column2.setCellValueFactory(new PropertyValueFactory<>("name"));
	    TableColumn<Book, String> column3 = new TableColumn<>("Author");
	    column3.setCellValueFactory(new PropertyValueFactory<>("author"));
	    TableColumn<Book, String> column4 = new TableColumn<>("Genre");
	    column4.setCellValueFactory(new PropertyValueFactory<>("genreName"));
	    TableColumn<Book, Integer> column5 = new TableColumn<>("Stock");
	    column5.setCellValueFactory(new PropertyValueFactory<>("stock"));
	    TableColumn<Book, Integer> column6 = new TableColumn<>("Price");
	    column6.setCellValueFactory(new PropertyValueFactory<>("price"));

	    marketView.getColumns().add(column1);
	    marketView.getColumns().add(column2);
	    marketView.getColumns().add(column3);
	    marketView.getColumns().add(column4);
	    marketView.getColumns().add(column5);
	    marketView.getColumns().add(column6);

	    //marketView.getItems().add(new Book(1, "Laskar Pelangi", "Andrea Hinata", "Fiction", 244, 40000));
	    //marketView.getItems().add(new Book(2, "Laskar Pelangi 2", "Andrea Hinata", "Fiction", 244, 40000));

	    Text txtTitle = new Text("Book List");
	    txtTitle.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
	    BuyBookForm.getChildren().add(txtTitle);
	    BuyBookForm.getChildren().add(marketView);
		VBox.setMargin(txtTitle, new Insets(20, 20, 0, 20));
		VBox.setMargin(marketView, new Insets(0, 20, 20, 20));
	    
	    CartView.setPrefWidth(900);
	    CartView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn<Cart, Integer> detailColumn1 = new TableColumn<>("Book ID");
	    detailColumn1.setCellValueFactory(new PropertyValueFactory<>("bookId"));
	    TableColumn<Cart, String> detailColumn2 = new TableColumn<>("Name");
	    detailColumn2.setCellValueFactory(new PropertyValueFactory<>("bookName"));
	    TableColumn<Cart, String> detailColumn3 = new TableColumn<>("Author");
	    detailColumn3.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
	    TableColumn<Cart, Integer> detailColumn4 = new TableColumn<>("Price");
	    detailColumn4.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
	    TableColumn<Cart, Integer> detailColumn5 = new TableColumn<>("Qty");
	    detailColumn5.setCellValueFactory(new PropertyValueFactory<>("qty"));

	    CartView.getColumns().add(detailColumn1);
	    CartView.getColumns().add(detailColumn2);
	    CartView.getColumns().add(detailColumn3);
	    CartView.getColumns().add(detailColumn4);
	    CartView.getColumns().add(detailColumn5);

	    //CartView.getItems().add(new Cart(1, "Laskar Pelangi", "Andrea Hinata", 40000, 2));
	    
	    Text txtTitle2 = new Text("My Cart");
	    VBox.setMargin(txtTitle2, new Insets(0, 20, 0, 20));
	    txtTitle2.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
	    BuyBookForm.getChildren().add(txtTitle2);
	    
	    VBox vbox = new VBox();
	    Label lblQty = new Label("Quantity");
	    vbox.getChildren().add(lblQty);
	    
	    Spinner<Integer> qtyInput = new Spinner<Integer>(1, 99, 1);
	    qtyInput.setPrefWidth(500);
	    qtyInput.setEditable(true);
        vbox.getChildren().add(qtyInput);
        
        Button btnAddToCart = new Button("Add to Cart");
        btnAddToCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(marketView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(!Validation.checkQuantity(marketView.getSelectionModel().getSelectedItem().getId(), qtyInput.getValue())) {
					return;
				}
				
				if(DatabaseConnection.addCart(userId, marketView.getSelectionModel().getSelectedItem().getId(), qtyInput.getValue())) {
					DatabaseConnection.getAllCarts(CartView, userId);
					qtyInput.getValueFactory().setValue(1);
				}
			}
		});
        VBox.setMargin(btnAddToCart, new Insets(20, 0, 0, 0));
        btnAddToCart.setPrefWidth(500);
        vbox.getChildren().add(btnAddToCart);
        
        Button btnUpdateCart = new Button("Update Cart");
        btnUpdateCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(CartView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(!Validation.checkQuantity(CartView.getSelectionModel().getSelectedItem().getBookId(), qtyInput.getValue())) {
					return;
				}
				
				if(DatabaseConnection.updateCart(userId, CartView.getSelectionModel().getSelectedItem().getBookId(), qtyInput.getValue())) {
					DatabaseConnection.getAllCarts(CartView, userId);
					qtyInput.getValueFactory().setValue(1);
				}
			}
		});
        btnUpdateCart.setPrefWidth(500);
        vbox.getChildren().add(btnUpdateCart);
        
        Button btnRemoveFromCart = new Button("Remove from Cart");
        btnRemoveFromCart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(CartView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(DatabaseConnection.removeCart(userId, CartView.getSelectionModel().getSelectedItem().getBookId())) {
					DatabaseConnection.getAllCarts(CartView, userId);
					qtyInput.getValueFactory().setValue(1);
				}
			}
		});
        btnRemoveFromCart.setPrefWidth(500);
        vbox.getChildren().add(btnRemoveFromCart);
        
        Button btnCheckout = new Button("Checkout");
        btnCheckout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(CartView.getItems().size() == 0) {
					AlertWindow.show(AlertType.ERROR, "Cart is empty!");
					return;
				}
				
				if(DatabaseConnection.checkoutCart(userId)) {
					AlertWindow.show(AlertType.INFORMATION, "Checkout success!");
					qtyInput.getValueFactory().setValue(1);
					DatabaseConnection.getAllCarts(CartView, userId);
				}
				
			}
		});
        btnCheckout.setPrefWidth(500);
        vbox.getChildren().add(btnCheckout);
	    
	    HBox hbox = new HBox();
	    hbox.getChildren().add(CartView);
	    HBox.setMargin(vbox, new Insets(0, 10, 0, 10));
	    hbox.getChildren().add(vbox);
	    VBox.setMargin(hbox, new Insets(0, 20, 20, 20));
	    BuyBookForm.getChildren().add(hbox);
	    
	}
	
	public void initHistoryForm() {
		historyWindow.setTitle("Transaction History");
		historyWindow.setContentPane(historyForm);
		
	    historyView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn<Transaction, Integer> column1 = new TableColumn<>("ID");
	    column1.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<Transaction, String> column2 = new TableColumn<>("Date");
	    column2.setCellValueFactory(new PropertyValueFactory<>("date"));

	    historyView.getColumns().add(column1);
	    historyView.getColumns().add(column2);

	    //historyView.getItems().add(new Transaction(1, "02-09-2003"));
	    //historyView.getItems().add(new Transaction(1231, "02-09-2003"));

	    historyView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	            DatabaseConnection.getAllDetailTransaction(detailView, newSelection.getId());
	        }
	    });
	    
		HBox.setMargin(historyView, new Insets(20, 0, 20, 20));
	    historyForm.getChildren().add(historyView);
	    
	    
	    detailView.setSelectionModel(null);
	    detailView.setPrefWidth(800);
	    detailView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn<TransactionDetail, Integer> detailColumn1 = new TableColumn<>("Book ID");
	    detailColumn1.setCellValueFactory(new PropertyValueFactory<>("bookId"));
	    TableColumn<TransactionDetail, String> detailColumn2 = new TableColumn<>("Name");
	    detailColumn2.setCellValueFactory(new PropertyValueFactory<>("bookName"));
	    TableColumn<TransactionDetail, String> detailColumn3 = new TableColumn<>("Author");
	    detailColumn3.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
	    TableColumn<TransactionDetail, Integer> detailColumn4 = new TableColumn<>("Price");
	    detailColumn4.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
	    TableColumn<TransactionDetail, Integer> detailColumn5 = new TableColumn<>("Qty");
	    detailColumn5.setCellValueFactory(new PropertyValueFactory<>("qty"));

	    detailView.getColumns().add(detailColumn1);
	    detailView.getColumns().add(detailColumn2);
	    detailView.getColumns().add(detailColumn3);
	    detailView.getColumns().add(detailColumn4);
	    detailView.getColumns().add(detailColumn5);

	    //detailView.getItems().add(new TransactionDetail(1, "Laskar Pelangi", "Andrea Hirata", "Fiction", 40000, 2));
	    
		HBox.setMargin(detailView, new Insets(20, 20, 20, 0));
	    historyForm.getChildren().add(detailView);
	}
	
	public void initBookForm() {
		bookWindow.setTitle("Manage Book");
		bookWindow.setContentPane(bookForm);
		bookWindow.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		
	    bookView.setPrefWidth(800);
	    bookView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn<Book, Integer> column1 = new TableColumn<>("ID");
	    column1.setCellValueFactory(new PropertyValueFactory<>("id"));
	    TableColumn<Book, String> column2 = new TableColumn<>("Name");
	    column2.setCellValueFactory(new PropertyValueFactory<>("name"));
	    TableColumn<Book, String> column3 = new TableColumn<>("Author");
	    column3.setCellValueFactory(new PropertyValueFactory<>("author"));
	    TableColumn<Book, String> column4 = new TableColumn<>("Genre");
	    column4.setCellValueFactory(new PropertyValueFactory<>("genreName"));
	    TableColumn<Book, Integer> column5 = new TableColumn<>("Stock");
	    column5.setCellValueFactory(new PropertyValueFactory<>("stock"));
	    TableColumn<Book, Integer> column6 = new TableColumn<>("Price");
	    column6.setCellValueFactory(new PropertyValueFactory<>("price"));

	    bookView.getColumns().add(column1);
	    bookView.getColumns().add(column2);
	    bookView.getColumns().add(column3);
	    bookView.getColumns().add(column4);
	    bookView.getColumns().add(column5);
	    bookView.getColumns().add(column6);

	    // tableView.getItems().add(new Book(1, "Laskar Pelangi", "Andrea Hinata", "Fiction", 244, 40000));
	    
	    VBox vbox = new VBox();
	    vbox.setAlignment(Pos.CENTER_LEFT);
	    // vbox.setSpacing(2);
	    HBox.setMargin(vbox, new Insets(20));
	    bookForm.getChildren().add(vbox);
	    bookForm.getChildren().add(bookView);
	    
	    Label lblId = new Label("ID");
	    vbox.getChildren().add(lblId);
	    
        idInput = new TextField();
        idInput.setDisable(true);
        vbox.getChildren().add(idInput);
	    
	    Label lblName = new Label("Name");
	    VBox.setMargin(lblName, new Insets(5, 0, 0, 0));
	    vbox.getChildren().add(lblName);
	    
        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        vbox.getChildren().add(nameInput);
        
	    Label lblAuthor = new Label("Author");
	    VBox.setMargin(lblAuthor, new Insets(5, 0, 0, 0));
	    vbox.getChildren().add(lblAuthor);
        
        TextField authorInput = new TextField();
        authorInput.setPromptText("Author");
        vbox.getChildren().add(authorInput);
        
	    Label lblGenre = new Label("Genre");
	    VBox.setMargin(lblGenre, new Insets(5, 0, 0, 0));
	    vbox.getChildren().add(lblGenre);
        
        genre = new ComboBox<>();
        genre.setConverter(new StringConverter<Genre>() {
			
			@Override
			public String toString(Genre object) {
				 return object == null ?  "" : object.getName(); 
			}
			
			@Override
			public Genre fromString(String string) {
				return null;
			}
		});
        genre.setPrefWidth(300);
        vbox.getChildren().add(genre);
        
	    Label lblPrice = new Label("Price");
	    VBox.setMargin(lblPrice, new Insets(5, 0, 0, 0));
	    vbox.getChildren().add(lblPrice);
        
        TextField priceInput = new TextField();
        priceInput.setPromptText("Price");
        vbox.getChildren().add(priceInput);
        
	    Label lblStock = new Label("Stock");
	    VBox.setMargin(lblStock, new Insets(5, 0, 0, 0));
	    vbox.getChildren().add(lblStock);
        
	    Spinner<Integer> stockInput = new Spinner<Integer>(0, 99, 0);
	    stockInput.setEditable(true);
	    stockInput.setPrefWidth(300);
        vbox.getChildren().add(stockInput);
        
	    bookView.setRowFactory(bv -> {
	        TableRow<Book> row = new TableRow<>();
	        row.setOnMouseClicked(event -> {
	            if (!row.isEmpty()) {
	                Book rowData = row.getItem();
	                idInput.setText(String.valueOf(rowData.getId()));
	                nameInput.setText(rowData.getName());
	                authorInput.setText(rowData.getAuthor());
	                genre.valueProperty().setValue(new Genre(rowData.getGenreId(), rowData.getGenreName()));
	                priceInput.setText(String.valueOf(rowData.getPrice()));
					stockInput.getValueFactory().setValue(rowData.getStock());
	                
	            }
	        });
	        return row;
	    });
        
        Button btnInsert = new Button("Insert");
        btnInsert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(Validation.validateBook(nameInput.getText(), authorInput.getText(), (genre.getValue() != null) ? genre.getValue().getName() : "", priceInput.getText(), stockInput.getValue()) && 
						DatabaseConnection.insertBook(nameInput.getText(), authorInput.getText(), genre.getValue().getId(), stockInput.getValue(), Integer.parseInt(priceInput.getText()))) {
					 DatabaseConnection.getAllBooks(bookView);
					AlertWindow.show(AlertType.INFORMATION, "Book "+nameInput.getText()+" added!");
					
					idInput.setText(String.valueOf(DatabaseConnection.getBookIncrement()));
					nameInput.clear();
					authorInput.clear();
					genre.valueProperty().set(null);
					priceInput.clear();
					stockInput.getValueFactory().setValue(0);
				}
			}
		});
        VBox.setMargin(btnInsert, new Insets(10, 0, 0, 0));
        btnInsert.setPrefWidth(300);
        vbox.getChildren().add(btnInsert);
        
        Button btnUpdate = new Button("Update");
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(bookView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(Validation.validateBook(nameInput.getText(), authorInput.getText(), (genre.getValue() != null) ? genre.getValue().getName() : "", priceInput.getText(), stockInput.getValue()) && 
						DatabaseConnection.updateBook(bookView.getSelectionModel().getSelectedItem().getId(), nameInput.getText(), authorInput.getText(), genre.getValue().getId(), stockInput.getValue(), Integer.parseInt(priceInput.getText()))) {
					 DatabaseConnection.getAllBooks(bookView);
					AlertWindow.show(AlertType.INFORMATION, "Book updated!");
					
					idInput.setText(String.valueOf(DatabaseConnection.getBookIncrement()));
					nameInput.clear();
					authorInput.clear();
					genre.valueProperty().set(null);
					priceInput.clear();
					stockInput.getValueFactory().setValue(0);
				}
			}
		});
        btnUpdate.setPrefWidth(300);
        vbox.getChildren().add(btnUpdate);
        
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(bookView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(DatabaseConnection.deleteBook(bookView.getSelectionModel().getSelectedItem().getId())) {
					 DatabaseConnection.getAllBooks(bookView);
					AlertWindow.show(AlertType.INFORMATION, "Book deleted!");
					
					idInput.setText(String.valueOf(DatabaseConnection.getBookIncrement()));
					nameInput.clear();
					authorInput.clear();
					genre.valueProperty().set(null);
					priceInput.clear();
					stockInput.getValueFactory().setValue(0);
				}
			}
		});
        btnDelete.setPrefWidth(300);
        vbox.getChildren().add(btnDelete);
	}
	
	public void initGenreForm() {
		genreWindow.setTitle("Manage Genre");
		genreWindow.setContentPane(genreForm);
		genreWindow.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
		
	    genreView.setPrefWidth(800);
	    genreView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn<Genre, String> column1 = new TableColumn<>("ID");
	    column1.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<Genre, String> column2 = new TableColumn<>("Name");
	    column2.setCellValueFactory(new PropertyValueFactory<>("name"));

	    genreView.getColumns().add(column1);
	    genreView.getColumns().add(column2);

	    //tableView.getItems().add(new Genre(1, "Fantasy"));
	    //tableView.getItems().add(new Genre(1231, "Comedy"));
	    
	    VBox vbox = new VBox();
	    vbox.setAlignment(Pos.CENTER_LEFT);
	    HBox.setMargin(vbox, new Insets(20));
	    genreForm.getChildren().add(vbox);
	    genreForm.getChildren().add(genreView);
	    
	    Label lblName = new Label("Name");
	    vbox.getChildren().add(lblName);
	    
        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        vbox.getChildren().add(nameInput);
        
        Button btnInsert = new Button("Insert");
        btnInsert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(Validation.validateGenre(nameInput.getText()) &&
						DatabaseConnection.insertGenre(nameInput.getText())) {
					DatabaseConnection.getAllGenres(genreView);
					AlertWindow.show(AlertType.INFORMATION, "Genre "+nameInput.getText()+" added!");
					nameInput.clear();
				}
			}
		});
        VBox.setMargin(btnInsert, new Insets(0, 0, 0, 0));
        btnInsert.setPrefWidth(400);
        vbox.getChildren().add(btnInsert);
        
        Button btnUpdate = new Button("Update");
        btnUpdate.setPrefWidth(400);
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(genreView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(Validation.validateGenre(nameInput.getText()) &&
						DatabaseConnection.updateGenre(genreView.getSelectionModel().getSelectedItem().getId(), nameInput.getText())) {
					DatabaseConnection.getAllGenres(genreView);
					AlertWindow.show(AlertType.INFORMATION, "Genre updated!");
				}
			}
		});
        //vbox.getChildren().add(btnUpdate);
        
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(genreView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
				
				if(DatabaseConnection.deleteGenre(genreView.getSelectionModel().getSelectedItem().getId())) {
					DatabaseConnection.getAllGenres(genreView);
					AlertWindow.show(AlertType.INFORMATION, "Genre deleted!");
				}
			}
		});
        btnDelete.setPrefWidth(400);
        //vbox.getChildren().add(btnDelete);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			currentStage = primaryStage;
			
			init();
			initLoginForm();
			initRegisterForm();
			initMainForm();
			initBuyBookForm();
			initHistoryForm();
			initBookForm();
			initGenreForm();
			
		    root.setCenter(loginForm);
		    rootMain.setCenter(mainForm);
		    primaryStage.getIcons().add(new Image("file:src/application/assets/icon.png"));
		    primaryStage.setTitle("Bookstore");
		    primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			DatabaseConnection.getInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
