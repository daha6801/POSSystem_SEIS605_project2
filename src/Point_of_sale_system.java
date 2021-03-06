package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.omg.CORBA.portable.ValueFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class Point_of_sale_system extends Application {
	Model model = new Model();
	View view = new View();
	ObservableList<ShoppingCart> shoppingCartObservableList = FXCollections.observableArrayList();
	TableColumn<Item, String> nameColumn;
	TableColumn<Item, Integer> quantityColumn;
	TableColumn<Item, Double> priceColumn;
	TableColumn<Item, Spinner> selectColumn;
	
	
	TableColumn<ShoppingCart, String> shoppingnameColumn;
    TableColumn<ShoppingCart, Integer> shoppingquantityColumn;
    TableColumn<ShoppingCart, Double> shoppingpriceColumn;
    
	public static void main(String[] args) {
		launch(args);
	}

	 public void start(Stage primaryStage) throws Exception {

		 model.loadData(); //this will load the data from .csv file
		 Scene scene = new Scene(view.setupScene(),1000, 1000);
		 
	     primaryStage.setWidth(1500);
	     primaryStage.setHeight(900);

	     setupActions();

	     createshoppingCartTable();
	     
	     calculateTotalPrice();
	     
	     final Label label = new Label("Welcome to Point of Sale System");
	     final VBox vbox = new VBox();
	     final VBox vboxshopping = new VBox();

	     view.tableView.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.30));
	     view.tableView.prefHeightProperty().bind(primaryStage.heightProperty());
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
		nameColumn = new TableColumn<>("Item name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
		
		priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("unitPrice"));
		selectColumn = new TableColumn<>("Select Item");
		

		//Callback for spinner column
		Callback<CellDataFeatures<Item, Spinner>, ObservableValue<Spinner>> callback = 
			new Callback <CellDataFeatures<Item,Spinner>, ObservableValue<Spinner>> () {

				@Override
					public ObservableValue<Spinner> call(CellDataFeatures<Item,Spinner> arg0) {
						Integer qnty = arg0.getValue().getUnitQuantity();
						Integer selectqunatity = arg0.getValue().getselectQuantity();
						
						Spinner<Integer> selectSpinner = new Spinner(0, qnty,selectqunatity);
						
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
						
						return new SimpleObjectProperty<Spinner>(selectSpinner);
				}

		};
		
		selectColumn.setCellValueFactory(callback);
		
		quantityColumn = new TableColumn<>("Quantity");

		quantityColumn.setCellValueFactory(new Callback<CellDataFeatures<Item, Integer>, ObservableValue<Integer>>() {

			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Item, Integer> arg0) {
				return (new SimpleIntegerProperty(arg0.getValue().getUnitQuantity() - arg0.getValue().getselectQuantity())).asObject();
			}
			
		});
		
		//add all columns to the tableview
		view.tableView.getColumns().setAll(nameColumn,quantityColumn,priceColumn, selectColumn);
		
		
	    view.addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
    	
	    	
			@Override
			public void handle(ActionEvent arg0) {	

				view.tableView.refresh();
				ShoppingCart shoppingCart = null;
				for (Item r : view.tableView.getItems()) {
		    	
					if (selectColumn.getCellData(r) != null) {
						if ( r.getUnitQuantity() > 0 && r.getselectQuantity() >= 0) {

							if (view.shoppingCarttableView.getItems().isEmpty()) {
								double totalPrice =  r.getselectQuantity() * r.getUnitPrice();
								shoppingCart = new ShoppingCart( r.getName(),  r.getselectQuantity() ,totalPrice);
								shoppingCartObservableList.add(shoppingCart);

								//Update the reamining items in the inventory
								int index = model.itemsObservableList.indexOf(r);
								model.itemsObservableList.set(index, new Item(r.getName(), r.getUnitQuantity(), r.getUnitPrice(), r.getselectQuantity()));
							}
							else {
								for (ShoppingCart s : view.shoppingCarttableView.getItems()) {
								
									
									if (s.getUnitQuantity() > 0) {
										if (r.getName().equals(s.getName())) {
											shoppingCartObservableList.remove(s);
											break;
										}
									}
								}
								
								if (r.getselectQuantity() == 0) {
									return;
								}
								
								//Create a new object of ShoppingCart class and add to the observable list
								double totalPrice =  r.getselectQuantity() * r.getUnitPrice();
								shoppingCart = new ShoppingCart( r.getName(),  r.getselectQuantity() ,totalPrice);
								shoppingCartObservableList.add(shoppingCart);

								//Update the remaining items in the inventory
								int index = model.itemsObservableList.indexOf(r);
								model.itemsObservableList.set(index, new Item(r.getName(), r.getUnitQuantity(), r.getUnitPrice(), r.getselectQuantity()));

							}
						}
					}

		    	}
				
			}
	    });
	    
	    view.cancelTransaction.setOnAction(new EventHandler<ActionEvent>() {
    	
	    	
			@Override
			public void handle(ActionEvent arg0) {	
				for (ShoppingCart s : view.shoppingCarttableView.getItems()) {
					
					for (Item r : view.tableView.getItems()) {
						if (r.getName().equals(s.getName())) {
							
							int index = model.itemsObservableList.indexOf(r); //get index of r
							model.itemsObservableList.set(index, new Item(r.getName(), r.getUnitQuantity() + s.getUnitQuantity(), r.getUnitPrice(), 0));
						}

					}
				}
				shoppingCartObservableList.clear();
				view.enterPricetextField.clear();
				view.balanceAmountLabel.setText("0.00");
			}
	    });
	    
	    
	    view.returnItembutton.setOnAction(new EventHandler<ActionEvent>() {
    	
	    	
			@Override
			public void handle(ActionEvent arg0) {
				
				
				Dialog<Pair<String, String>> dialog = new Dialog<>();
			    dialog.setTitle("Return Items");

			    // Set the button types.
			    ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
			    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

			    GridPane gridPane = new GridPane();
			    gridPane.setHgap(10);
			    gridPane.setVgap(10);
			    gridPane.setPadding(new Insets(20, 150, 10, 10));

			    gridPane.add(view.userReturnedItem, 0, 0);
			    gridPane.add(view.userEReturnedItemTextField, 1, 0);
			    gridPane.add(view.userReturnedQuantity, 0, 1);
			    gridPane.add(view.userReturnedQuantityTextField, 1, 1);

			    dialog.getDialogPane().setContent(gridPane);

			    dialog.setResultConverter(dialogButton -> {
			        if (dialogButton == loginButtonType) {
			            return new Pair<>(view.userEReturnedItemTextField.getText(), view.userReturnedQuantityTextField.getText());
			        }
			        return null;
			    });
			    
			    Optional<Pair<String, String>> result = dialog.showAndWait();
			    
			    result.ifPresent(pair -> {

			        Double return_balance = 0.00;
			        for (Item r : view.tableView.getItems()) {
				    	
						if (r.getName().equals(pair.getKey())) {

							int index = model.itemsObservableList.indexOf(r); 
							model.itemsObservableList.set(index, new Item(r.getName(), (r.getUnitQuantity() + Integer.parseInt(pair.getValue())), r.getUnitPrice(), 0));
							return_balance = Integer.parseInt(pair.getValue()) * r.getUnitPrice();
							break;
						}
				    }
			        
			        view.balanceAmountLabel.setText(Double.toString(return_balance));
			
			        view.tableView.refresh();
			    });
	
			}
	    });
	    
	    view.AddInventoryButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// Create the custom dialog.
			    Dialog<Pair<String, String>> dialog = new Dialog<>();
			    dialog.setTitle("Add Inventory");

			    // Set the button types.
			    ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
			    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

			    GridPane gridPane = new GridPane();
			    gridPane.setHgap(10);
			    gridPane.setVgap(10);
			    gridPane.setPadding(new Insets(20, 150, 10, 10));

			    gridPane.add(view.userEnteredItem, 0, 0);
			    gridPane.add(view.userEnteredItemTextField, 1, 0);
			    gridPane.add(view.userEnteredQuantity, 0, 1);
			    gridPane.add(view.userEnteredQuantityTextField, 1, 1);

			    dialog.getDialogPane().setContent(gridPane);

			    dialog.setResultConverter(dialogButton -> {
			        if (dialogButton == loginButtonType) {
			            return new Pair<>(view.userEnteredItemTextField.getText(), view.userEnteredQuantityTextField.getText());
			        }
			        return null;
			    });

			    Optional<Pair<String, String>> result = dialog.showAndWait();
			    
			    result.ifPresent(pair -> {
			        Boolean item_already_in_the_list = false;
			        for (Item r : view.tableView.getItems()) {
				    	
						if (r.getName().equals(pair.getKey())) {
							item_already_in_the_list = true;
							break;
						}
				    }
			        
			        if (!item_already_in_the_list) {
			        	Item newitem = new Item(pair.getKey(), Integer.parseInt(pair.getValue()), 2.69, 0);
			        	model.itemsObservableList.add(newitem);
			        }
			        
			        view.tableView.refresh();
			    });
				
			    
			}
	    	
	    	
	    });
	    
	    
	    view.DeleteInventoryButton.setOnAction(new EventHandler<ActionEvent>() {

	    	
	    	Boolean item_already_in_the_list = false;
			@Override
			public void handle(ActionEvent arg0) {
				
				TextInputDialog dialog = new TextInputDialog("");
				 
		        dialog.setTitle("Delete Inventory");
		        dialog.setHeaderText("Enter the name of the item you want to delete:");
		        dialog.setContentText("Name:");
		 
		        Optional<String> result = dialog.showAndWait();
		 
		        result.ifPresent(name -> {
		        	
		        	for (Item r : view.tableView.getItems()) {
				    	
						if (r.getName().equals(name)) {
							model.itemsObservableList.remove(r);
							break;
						}
				    }
			        
		        });

				
			}
	    	
	    	
	    });
	    
	    
	    view.checkOutButton.setOnAction(new EventHandler<ActionEvent>() {

			
			public void handle(ActionEvent arg0) {
				
		        if (!view.enterPricetextField.getText().isEmpty()) {
		        	double getCash = Double.parseDouble(view.enterPricetextField.getText());
		        	double totalprice = Double.parseDouble(view.totalPriceAmountLabel.getText());
		        	if (getCash > totalprice) {
		        		double balance = getCash - totalprice;
		        		view.balanceAmountLabel.setText(Double.toString(balance));
		        		shoppingCartObservableList.clear(); //remove items from the shopping cart once transaction is complete
		        		view.enterPricetextField.clear();
		        	}
		        	else {
		        		view.balanceAmountLabel.setText("Not enough!");
		        	}
		        }
		        
		       Date date = new Date();
		       SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
               String overAllPrice = view.totalPriceAmountLabel.getText();
               //double n = Math.random();
               //long n1 = Math.round(Math.random()*10);
               int n1 = 1;
               long n6 = Math.round(Math.random()*1000000);
               String cashierName = view.nameField.getText();

               File directory = new File("C:\\Drawer");
               if (! directory.exists()){
            	   directory.mkdir();
               }

               String drawerData;
               if (cashierName.equals("bvang")) {
            	   	drawerData = "C:\\Drawer\\bvang_drawer.csv";
               } else if (cashierName.equals("rdahal")) {
            	   drawerData = "C:\\Drawer\\rdahal_drawer.csv";
               } else if (cashierName.equals("jlie")) {
            	   drawerData = "C:\\Drawer\\jlie_drawer.csv";
               } else {
            	   drawerData = "C:\\Drawer\\drawer.csv";
               }

               try (BufferedWriter bf = new BufferedWriter(new FileWriter(drawerData, true)))
               {
            	   bf.newLine();
            	   bf.write("*************Point Of Sale System*************,");
            	   bf.newLine();
            	   bf.write(ft.format(date));
            	   bf.newLine();
            	   bf.write("Register ID: " + n1);
            	   bf.newLine();
            	   bf.write("Cashier Name: " + cashierName);
            	   bf.newLine();
            	   bf.write("Sale ID: " + n6);
            	   bf.newLine();
            	   bf.write("Total Sales: $" + overAllPrice);
            	   bf.newLine();
            	   bf.close();
               }
               catch (IOException ex)
               {

               }

               File totalAmountOfSale = new File("C:\\Drawer\\totalAmountOfSales.txt");
               try (BufferedWriter df = new BufferedWriter(new FileWriter(totalAmountOfSale, true)))
               {
            	   df.write(overAllPrice);
            	   df.newLine();
            	   df.close();
               }
               catch (IOException ex)
               {
            	   //
               }

			}
	    });
	    
	  //bind export button to output inventory csv
	    view.export.setOnAction(new EventHandler<ActionEvent>() {
	    	@Override
	    	public void handle(ActionEvent e)
	    	{
	    		try
	    		{
	    			writeExcel();
	    		}
	    		catch (Exception ex)
	    		{
	    			ex.printStackTrace();
	    		}
	    	}

	    });
	   	   
	}
	
	 private void createshoppingCartTable() {
			
			//bind table view to data
			view.shoppingCarttableView.setItems(shoppingCartObservableList);
			
			//Create columns and bind them to their Property ValueFactory
			shoppingnameColumn = new TableColumn<>("Item name");
			shoppingnameColumn.setCellValueFactory(new PropertyValueFactory<ShoppingCart, String>("name"));
			shoppingquantityColumn = new TableColumn<>("Quantity");
			shoppingquantityColumn.setCellValueFactory(new PropertyValueFactory<ShoppingCart, Integer>("unitQuantity"));
			shoppingpriceColumn = new TableColumn<>("Total Price");
			shoppingpriceColumn.setCellValueFactory(new PropertyValueFactory<ShoppingCart, Double>("totalPrice"));
			
			//add all columns to the tableviewrdaha
			view.shoppingCarttableView.getColumns().setAll(shoppingnameColumn,shoppingquantityColumn,shoppingpriceColumn);
						  
		   
	  }
	 
	 private void calculateTotalPrice() {
		 //bind the textField data with total amount paid
		  	DoubleBinding totalPriceAmountLabelBinding = new DoubleBinding() {
		  			
		  		
		  			{
		  				super.bind(shoppingCartObservableList, view.totalPriceAmountLabel.textProperty());
		  			}
		  		
		  			@Override
		  			protected double computeValue() {
		  				
		  				double totalPrice = 0;
		  				for (ShoppingCart r : view.shoppingCarttableView.getItems()) {
		  			    	
							if (shoppingpriceColumn.getCellData(r) != null) {
								if ( r.getTotalPrice() > 0 ) {

									totalPrice = totalPrice + r.getTotalPrice();  
				    			
								}
							}
				    	}
						return totalPrice;
		  				
		  			}
		  	};
		  	
		  //bind the totalPrice to totalPriceLabel
			view.totalPriceAmountLabel.textProperty().bind(Bindings.format("%.2f", totalPriceAmountLabelBinding));
	 }
	 
	 public void writeExcel() throws Exception {
		Writer writer = null;
		try {
			File file = new File("src/Inventory.csv");
			writer = new BufferedWriter(new FileWriter(file));
	
	
			for (Item r : view.tableView.getItems()) {
	
				String text = r.getName() + "," + "Quantity: " + r.getUnitQuantity() + "," + "Threshold: 5" + "," + "Supplier: Cub Foods" + "," + "Outstanding Order: 5" + "\n";
	
				writer.write(text);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
	
			writer.flush();
			writer.close();
		}
	}
	 	 
}


