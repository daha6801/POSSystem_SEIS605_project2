package src;

import org.omg.CORBA.portable.ValueFactory;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Point_of_sale_system extends Application {
	Model model = new Model();
	View view = new View();
	ObservableList<ShoppingCart> shoppingCartObservableList = FXCollections.observableArrayList();
	
	public static void main(String[] args) {
		launch(args);
	}

	 public void start(Stage primaryStage) throws Exception {

		 model.loadData(); //this will load the data from .csv file
		 Scene scene = new Scene(view.setupScene(),1000, 1000);
		 
	     primaryStage.setWidth(1500);
	     primaryStage.setHeight(900);

	     setupActions();

	     final Label label = new Label("Welcome to Point of Sale System");
	     final VBox vbox = new VBox();
	     final VBox vboxshopping = new VBox();
	     //vbox.setSpacing(20);
	    // vbox.setPadding(new Insets(20, 20, 20, 20));
	     view.tableView.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.30));
	     view.tableView.prefHeightProperty().bind(primaryStage.heightProperty());
	     //vbox.setPrefWidth(400);// prefWidth
	     //vbox.setPrefHeight(500);// prefHeight
	     view.shoppingCarttableView.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.30));
	     view.shoppingCarttableView.prefHeightProperty().bind(primaryStage.heightProperty());
	     vbox.getChildren().addAll(label, view.tableView);
	     vboxshopping.getChildren().addAll(label, view.shoppingCarttableView);
	        
	   
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
						Spinner<Integer> selectSpinner = new Spinner(0, qnty, 0);
						
						//selectSpinner.setValueFactory(spinnerFactorySelect);	
						selectSpinner.setEditable(true);

						selectSpinner.getEditor().textProperty().addListener((obs, oldval, newval) -> {
							SpinnerValueFactory<Integer> valueFactory = selectSpinner.getValueFactory();
							
							if (valueFactory != null) {
								StringConverter<Integer> converter = valueFactory.getConverter();
								if (converter != null ) {
									try {
										Integer value = converter.fromString(newval);
										if (value != null) {
											valueFactory.setValue(value);
											
										}
										else 
											valueFactory.setValue(0);
									}
									catch (NumberFormatException e) {
										selectSpinner.getEditor().setText(converter.toString(valueFactory.getValue()));
									}
								}
								
							}
							arg0.getValue().setselectQuantity(Integer.parseInt(selectSpinner.getEditor().getText())) ;
						});
						//model.itemsObservableList.setselectQuantity = ;
						
						return new SimpleObjectProperty<Spinner>(selectSpinner);
				}

		};
		
		selectColumn.setCellValueFactory(callback);
			
		//add all columns to the tableview
		view.tableView.getColumns().setAll(nameColumn,quantityColumn,priceColumn, selectColumn);
		
	    view.addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
    	
			@Override
			public void handle(ActionEvent arg0) {	

				for (Item r : view.tableView.getItems()) {
		    	
					if (selectColumn.getCellData(r) != null) {
						if ( r.getUnitQuantity() > 0 ) {

							double totalPrice =  r.getselectQuantity() * r.getUnitPrice();
							ShoppingCart shoppingCart = new ShoppingCart( r.getName(),  r.getselectQuantity() ,totalPrice);
							shoppingCartObservableList.add(shoppingCart);
		    			
						}
					}
		    	}
				createshoppingCartTable();
				
			}
	    });
	   	   
	}
	
	 private void createshoppingCartTable() {
			
			//bind table view to data
			view.shoppingCarttableView.setItems(shoppingCartObservableList);
			
			//Create columns and bind them to their Property ValueFactory
			TableColumn<ShoppingCart, String> shoppingnameColumn = new TableColumn<>("Item name");
			shoppingnameColumn.setCellValueFactory(new PropertyValueFactory<ShoppingCart, String>("name"));
			TableColumn<ShoppingCart, Integer> shoppingquantityColumn = new TableColumn<>("Quantity");
			shoppingquantityColumn.setCellValueFactory(new PropertyValueFactory<ShoppingCart, Integer>("unitQuantity"));
			TableColumn<ShoppingCart, Double> shoppingpriceColumn = new TableColumn<>("Total Price");
			shoppingpriceColumn.setCellValueFactory(new PropertyValueFactory<ShoppingCart, Double>("totalPrice"));
			
			//add all columns to the tableviewrdaha
			view.shoppingCarttableView.getColumns().setAll(shoppingnameColumn,shoppingquantityColumn,shoppingpriceColumn);
			
			
	  }
	 
}