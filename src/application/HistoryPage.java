package application;

import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import jfxtras.labs.scene.control.window.Window;
import model.Transaction;
import model.TransactionDetail;

public class HistoryPage {
	public HBox historyForm;

	public static Window historyWindow;
	public static TableView<Transaction> historyView;
	public static TableView<TransactionDetail> detailView;
	
	TableColumn<Transaction, Integer> column1;
	TableColumn<Transaction, String> column2;
	TableColumn<TransactionDetail, Integer> detailColumn1;
	TableColumn<TransactionDetail, String> detailColumn2;
	TableColumn<TransactionDetail, String> detailColumn3;
	TableColumn<TransactionDetail, Integer> detailColumn4;
	TableColumn<TransactionDetail, Integer> detailColumn5;
	
	public HistoryPage() {
		initComp();
		addComp();
		arrangeComp();
		action();
	}

	public void initComp() {
		historyForm = new HBox();
		
		historyWindow = new Window();
		historyView = new TableView<>();
		detailView = new TableView<>();
		
		column1 = new TableColumn<>("ID");
		column2 = new TableColumn<>("Date");

		detailColumn1 = new TableColumn<>("Book ID");
		detailColumn2 = new TableColumn<>("Name");
		detailColumn3 = new TableColumn<>("Author");
		detailColumn4 = new TableColumn<>("Price");
		detailColumn5 = new TableColumn<>("Qty");
	}
	
	private void addComp() {
		historyView.getColumns().add(column1);
		historyView.getColumns().add(column2);
		historyForm.getChildren().add(historyView);
		
		detailView.getColumns().add(detailColumn1);
		detailView.getColumns().add(detailColumn2);
		detailView.getColumns().add(detailColumn3);
		detailView.getColumns().add(detailColumn4);
		detailView.getColumns().add(detailColumn5);
		historyForm.getChildren().add(detailView);
	}

	private void arrangeComp() {
		historyWindow.setTitle("Transaction History");
		historyWindow.setContentPane(historyForm);
		
		historyView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		column1.setCellValueFactory(new PropertyValueFactory<>("id"));
		column2.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		HBox.setMargin(historyView, new Insets(20, 0, 20, 20));
		
		detailView.setSelectionModel(null);
		detailView.setPrefWidth(800);
		detailView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
		detailColumn1.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		detailColumn2.setCellValueFactory(new PropertyValueFactory<>("bookName"));
		detailColumn3.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
		detailColumn4.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
		detailColumn5.setCellValueFactory(new PropertyValueFactory<>("qty"));
	
		HBox.setMargin(detailView, new Insets(20, 20, 20, 0));
	}

	private void action() {
		historyView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				DatabaseConnection.getAllDetailTransaction(detailView, newSelection.getId());
			}
		});
	}
}