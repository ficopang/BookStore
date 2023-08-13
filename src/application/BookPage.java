package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import model.Genre;

public class BookPage {
	public HBox bookForm;
	
	public static Window bookWindow;
	public static TableView<Book> bookView;
	
	public static ComboBox<Genre> genre;
	TextField idInput;
	
	TableColumn<Book, Integer> column1;
	TableColumn<Book, String> column2;
	TableColumn<Book, String> column3;
	TableColumn<Book, String> column4;
	TableColumn<Book, Integer> column5;
	TableColumn<Book, Integer> column6;
	
	VBox vbox;
	
	Label lblId, lblName, lblAuthor, lblGenre, lblPrice, lblStock;
	TextField nameInput, authorInput, priceInput;
	Spinner<Integer> stockInput;
	Button btnInsert, btnUpdate, btnDelete;

	public BookPage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public void initComp() {
		bookForm = new HBox();
		bookView = new TableView<>();
		bookWindow = new Window();
		
		column1 = new TableColumn<>("ID");
		column2 = new TableColumn<>("Name");
		column3 = new TableColumn<>("Author");
		column4 = new TableColumn<>("Genre");
		column5 = new TableColumn<>("Stock");
		column6 = new TableColumn<>("Price");
	
		vbox = new VBox();
	
		lblId = new Label("ID");	
		idInput = new TextField();	
		lblName = new Label("Name");	
		nameInput = new TextField();	
		lblAuthor = new Label("Author");	
		authorInput = new TextField();	
		lblGenre = new Label("Genre");	
		genre = new ComboBox<>();	
		lblPrice = new Label("Price");	
		priceInput = new TextField();	
		lblStock = new Label("Stock");	
		stockInput = new Spinner<Integer>(0, 99, 0);	
		btnInsert = new Button("Insert");	
		btnUpdate = new Button("Update");	
		btnDelete = new Button("Delete");
	}
	
	private void addComp() {
		bookView.getColumns().add(column1);
		bookView.getColumns().add(column2);
		bookView.getColumns().add(column3);
		bookView.getColumns().add(column4);
		bookView.getColumns().add(column5);
		bookView.getColumns().add(column6);

		bookForm.getChildren().add(vbox);
		bookForm.getChildren().add(bookView);
		
		vbox.getChildren().add(lblId);
		vbox.getChildren().add(idInput);
		vbox.getChildren().add(lblName);
		vbox.getChildren().add(nameInput);
		vbox.getChildren().add(lblAuthor);
		vbox.getChildren().add(authorInput);
		vbox.getChildren().add(lblGenre);
		vbox.getChildren().add(genre);
		vbox.getChildren().add(lblPrice);
		vbox.getChildren().add(priceInput);
		vbox.getChildren().add(lblStock);
		vbox.getChildren().add(stockInput);
		vbox.getChildren().add(btnInsert);
		vbox.getChildren().add(btnUpdate);
		vbox.getChildren().add(btnDelete);
	}

	private void arrangeComp() {
		bookWindow.setTitle("Manage Book");
		bookWindow.setContentPane(bookForm);
		bookWindow.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
	
		bookView.setPrefWidth(800);
		bookView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
		column1.setCellValueFactory(new PropertyValueFactory<>("id"));
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));
		column3.setCellValueFactory(new PropertyValueFactory<>("author"));
		column4.setCellValueFactory(new PropertyValueFactory<>("genreName"));
		column5.setCellValueFactory(new PropertyValueFactory<>("stock"));
		column6.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		vbox.setAlignment(Pos.CENTER_LEFT);
		HBox.setMargin(vbox, new Insets(20));
		
		idInput.setDisable(true);
		VBox.setMargin(lblName, new Insets(5, 0, 0, 0));
		VBox.setMargin(lblAuthor, new Insets(5, 0, 0, 0));
		nameInput.setPromptText("Name");
		authorInput.setPromptText("Author");
		VBox.setMargin(lblGenre, new Insets(5, 0, 0, 0));
		genre.setPrefWidth(300);
		VBox.setMargin(lblPrice, new Insets(5, 0, 0, 0));
		priceInput.setPromptText("Price");
		VBox.setMargin(lblStock, new Insets(5, 0, 0, 0));
		stockInput.setEditable(true);
		stockInput.setPrefWidth(300);
		VBox.setMargin(btnInsert, new Insets(10, 0, 0, 0));
		btnInsert.setPrefWidth(300);
		btnUpdate.setPrefWidth(300);
		btnDelete.setPrefWidth(300);
	}

	private void action() {
		genre.setConverter(new StringConverter<Genre>() {
			
			@Override
			public String toString(Genre object) {
				return object == null ? "" : object.getName();
			}
	
			@Override
			public Genre fromString(String string) {
				return null;
			}
		});
		
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
		
		btnInsert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Validation.validateBook(nameInput.getText(), authorInput.getText(),
						(genre.getValue() != null) ? genre.getValue().getName() : "", priceInput.getText(),
						stockInput.getValue())
						&& DatabaseConnection.insertBook(nameInput.getText(), authorInput.getText(),
								genre.getValue().getId(), stockInput.getValue(),
								Integer.parseInt(priceInput.getText()))) {
					DatabaseConnection.getAllBooks(bookView);
					AlertWindow.show(AlertType.INFORMATION, "Book " + nameInput.getText() + " added!");
	
					idInput.setText(String.valueOf(DatabaseConnection.getBookIncrement()));
					nameInput.clear();
					authorInput.clear();
					genre.valueProperty().set(null);
					priceInput.clear();
					stockInput.getValueFactory().setValue(0);
				}
			}
		});
		
		btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (bookView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
	
				if (Validation.validateBook(nameInput.getText(), authorInput.getText(),
						(genre.getValue() != null) ? genre.getValue().getName() : "", priceInput.getText(),
						stockInput.getValue())
						&& DatabaseConnection.updateBook(bookView.getSelectionModel().getSelectedItem().getId(),
								nameInput.getText(), authorInput.getText(), genre.getValue().getId(),
								stockInput.getValue(), Integer.parseInt(priceInput.getText()))) {
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
		
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (bookView.getSelectionModel().getSelectedItem() == null) {
					AlertWindow.show(AlertType.ERROR, "Please select book first!");
					return;
				}
	
				if (DatabaseConnection.deleteBook(bookView.getSelectionModel().getSelectedItem().getId())) {
					DatabaseConnection.getAllBooks(bookView);
					AlertWindow.show(AlertType.INFORMATION, "Book deleted!");
	
					idInput.setText(String.valueOf(DatabaseConnection.getBookIncrement()));
					nameInput.clear();
					authorInput.clear();
					genre.valueProperty().set(null);
					stockInput.getValueFactory().setValue(0);
				}
			}
		});
	}
}