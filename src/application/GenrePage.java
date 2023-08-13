package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.control.window.Window;
import model.Genre;

public class GenrePage {
	public HBox genreForm;
	
	public static Window genreWindow;
	public static TableView<Genre> genreView;

	TableColumn<Genre, String> column1;
	TableColumn<Genre, String> column2;
	
	VBox vbox;	
	Label lblName;
	TextField nameInput;
	Button btnInsert;
	
	public GenrePage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public void initComp() {
		genreForm = new HBox();
		
		genreWindow = new Window();
		genreView = new TableView<>();
	
		column1 = new TableColumn<>("ID");
		column2 = new TableColumn<>("Name");
	
		vbox = new VBox();
		lblName = new Label("Name");
		nameInput = new TextField();
		btnInsert = new Button("Insert");
	}
	
	private void addComp() {
		genreView.getColumns().add(column1);
		genreView.getColumns().add(column2);

		genreForm.getChildren().add(vbox);
		genreForm.getChildren().add(genreView);
		vbox.getChildren().add(lblName);
		vbox.getChildren().add(nameInput);
		vbox.getChildren().add(btnInsert);
	}

	private void arrangeComp() {
		genreWindow.setTitle("Manage Genre");
		genreWindow.setContentPane(genreForm);
		genreWindow.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
	
		genreView.setPrefWidth(800);
		genreView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		column1.setCellValueFactory(new PropertyValueFactory<>("id"));
		column2.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		vbox.setAlignment(Pos.CENTER_LEFT);
		HBox.setMargin(vbox, new Insets(20));
		
		nameInput.setPromptText("Name");
		VBox.setMargin(btnInsert, new Insets(0, 0, 0, 0));
		btnInsert.setPrefWidth(400);
	}

	private void action() {
		btnInsert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Validation.validateGenre(nameInput.getText())
						&& DatabaseConnection.insertGenre(nameInput.getText())) {
					DatabaseConnection.getAllGenres(genreView);
					AlertWindow.show(AlertType.INFORMATION, "Genre " + nameInput.getText() + " added!");
					nameInput.clear();
				}
			}
		});
	}
}