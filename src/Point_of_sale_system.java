package src;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Point_of_sale_system extends Application {
	Model model = new Model();
	View view = new View();
	
	public static void main(String[] args) {
		launch(args);
	}

	 public void start(Stage primaryStage) throws Exception {

		 model.loadData(); //this will load the data from .csv file
		 Scene scene = new Scene(view.setupScene(), 800, 700);
		 
	     primaryStage.setWidth(1000);
	     primaryStage.setHeight(600);

	     setupActions();

	     final Label label = new Label("Welcome to Point of Sale System");
	     final VBox vbox = new VBox();
	     vbox.setSpacing(5);
	     vbox.setPadding(new Insets(10, 0, 0, 10));
	     vbox.getChildren().addAll(label, view.tableView);
	        
	   
	     primaryStage.setScene(scene);
	     primaryStage.setTitle("Point Of Sale System");
	     primaryStage.show(); 
	 }

	private void setupActions() {
			
		//bind table view to data
		view.tableView.setItems(model.itemsObservableList);
		
		//Create columns and bind them to their Property ValueFactory
		TableColumn<Item, String> nameColumn = new TableColumn<>("Item name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
		TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("unitQuantity"));
		TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("unitPrice"));
		TableColumn<Item, Spinner> selectColumn = new TableColumn<>("Select Item");
		
		
		
		//Callback for spinner column
		Callback<CellDataFeatures<Item, Spinner>, ObservableValue<Spinner>> callback = 
			new Callback <CellDataFeatures<Item,Spinner>, ObservableValue<Spinner>> () {

				@Override
				public ObservableValue<Spinner> call(CellDataFeatures<Item,Spinner> arg0) {
						Integer qnty = arg0.getValue().getUnitQuantity();
						Spinner<Integer> selectSpinner = new Spinner();
						IntegerSpinnerValueFactory spinnerFactorySelect = new IntegerSpinnerValueFactory(0,qnty,1);
						selectSpinner.setValueFactory(spinnerFactorySelect);
						return new SimpleObjectProperty<Spinner>(selectSpinner);
				}
			
		};
		
		selectColumn.setCellValueFactory(callback);
		
		//add all columns to the tableview
		view.tableView.getColumns().setAll(nameColumn,quantityColumn,priceColumn, selectColumn);
				
	}

}