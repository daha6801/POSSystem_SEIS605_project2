package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import javafx.scene.layout.VBox;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;

public class View {
	
	Label title = new Label("Welcome to the Point-of-Sale Registration System");
	Label nameLabel = new Label("Username");
	TextField nameField = new TextField();

	
	Label passwordLabel = new Label("Password");
	PasswordField passwordField = new PasswordField();

	Label userEnteredItem = new Label("New Item");
	TextField userEnteredItemTextField = new TextField();
	
	Label userEnteredQuantity = new Label("Quantity");
	TextField userEnteredQuantityTextField = new TextField();
	
	Label userEnteredIPrice= new Label("Price");
	TextField userEnteredIPriceTextField = new TextField();
	
	Label userReturnedItem = new Label("New Item");
	TextField userEReturnedItemTextField = new TextField();
	
	Label userReturnedQuantity = new Label("Quantity");
	TextField userReturnedQuantityTextField = new TextField();
	
	
	
	Button loginButton = new Button("Log in");
	Button createUser = new Button("New User");
	
	CheckBox rememberMe = new CheckBox("Remember Me");
	
	Button addToCartButton = new Button("Update Cart");
	Button AddInventoryButton = new Button("Add Inventory");
	Button DeleteInventoryButton = new Button("Remove Inventory");
	
	
	TableView<Item> tableView = new TableView<>();
		
	TableView<ShoppingCart> shoppingCarttableView = new TableView<>();
	
	Label totalPriceLable = new Label("Total Price");
	Label totalPriceAmountLabel = new Label("0.00");
	
	Button checkOutButton = new Button("Checkout");
	Button cancelTransaction = new Button("Cancel Transaction");
	Button returnItembutton = new Button("Return item");
	
	
	Label enterCashLabel = new Label("Enter Cash");
	TextField enterPricetextField = new TextField("0.00");
	
	Label balanceLabel = new Label("Balance");
	Label balanceAmountLabel = new Label("0.00");
	
	Button export = new Button("Inventory Report");
	
	Button cashier = new Button("Cashier Report");
	
	Button register = new Button("Register Report");
	
	BorderPane setupScene() {
		
		BorderPane root = new BorderPane();
		GridPane topGrid = new GridPane();
		topGrid.setAlignment(Pos.CENTER);
		root.setTop(topGrid);
		
        
		/**setup TopGrid*/
		topGrid.setVgap(1);
		topGrid.setHgap(1);
		
		//add controls to topGrid	

		title.setFont(Font.font("sans-serif", FontWeight.BOLD, 25));
		title.setTextFill(Color.BLACK);
		
	    topGrid.add(title, 0,0,2,1);
	    GridPane.setHalignment(title, HPos.LEFT);
	    GridPane.setMargin(title, new Insets(20, 0,10,0));
	    
	    nameLabel.setPrefHeight(40);
	    nameLabel.setPrefWidth(100);
		topGrid.add(nameLabel, 0, 2);
		GridPane.setHalignment(loginButton, HPos.RIGHT);
		
		
		nameField.setPrefHeight(40);
		nameField.setPrefWidth(10);
		topGrid.add(nameField, 0,3);
		
		
    
	    
		topGrid.add(passwordLabel, 0, 7);
		passwordLabel.setPrefHeight(40);
	    passwordField.setPrefHeight(40);
	    passwordField.setPrefWidth(10);
	    topGrid.add(passwordField, 0, 8);	    
	    
		topGrid.add(loginButton, 0,40,1,1);
		GridPane.setHalignment(loginButton, HPos.RIGHT);
		
					
		loginButton.setPrefHeight(40);
		loginButton.setDefaultButton(true);
		loginButton.setPrefWidth(100);
    
		topGrid.add(rememberMe,0, 15, 1, 1);
		GridPane.setHalignment(rememberMe, HPos.RIGHT);
	    
	    createUser.setPrefHeight(40);
	    createUser.setDefaultButton(true);
	    createUser.setPrefWidth(100);
		
	    topGrid.add(createUser,0, 40, 1, 1);
	    GridPane.setHalignment(createUser, HPos.LEFT);
	    
	    createUser.setPrefHeight(40);
	    createUser.setDefaultButton(true);
	    createUser.setPrefWidth(100);
    	   		
	    LoadUsers userList = new LoadUsers();
	    //Load User information
	    try {
			userList.loadUserData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	  
	    
	    loginButton.setOnAction(new EventHandler<ActionEvent>() {  //login button
	    	Boolean found_a_match = false;
            @Override
            public void handle(ActionEvent event) {
                if(nameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, topGrid.getScene().getWindow(), "Form Error!", "Please enter your name");
                    return;
                }
                if(passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, topGrid.getScene().getWindow(), "Form Error!", "Please enter a password");
                    return;
                }


                for(int i = 0; i< userList.getLenght(); i++) {
                	
                	if (((User) userList.getUser(i)).getUserName().contentEquals(nameField.getText()) && ((User) userList.getUser(i)).getUPassword().contentEquals(passwordField.getText())) {

                		found_a_match = true;
                		break;
                	}

                }  
                
                if (found_a_match) {

            		Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText("Welcome to the POS System");
                    
                    Optional<ButtonType> result = alert.showAndWait();
                    
                    if (result.get() == ButtonType.OK) {

                		GridPane topGrid = new GridPane();
                		root.setTop(topGrid);
                		
                		topGrid.add(tableView, 0, 0);
                		
                		VBox vbButtons4 = new VBox();
                		vbButtons4.setSpacing(100);
                		vbButtons4.setPadding(new Insets(330,20,20,20));
                		vbButtons4.getChildren().addAll(addToCartButton, AddInventoryButton, DeleteInventoryButton);
                		topGrid.add(vbButtons4, 1, 0);
                		
                		topGrid.add(shoppingCarttableView, 2, 0);
                		
                	    
                		GridPane.setHalignment(addToCartButton, HPos.RIGHT);
                		
                					
                		addToCartButton.setPrefHeight(40);
                		addToCartButton.setDefaultButton(true);
                		addToCartButton.setPrefWidth(100);
                		
						VBox vbButtons = new VBox();
						vbButtons.setSpacing(50);
						vbButtons.setPadding(new Insets(330,20,20,10));
						vbButtons.getChildren().addAll(totalPriceLable, totalPriceAmountLabel, enterCashLabel, enterPricetextField);
						totalPriceLable.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
						enterCashLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
						enterPricetextField.setPrefWidth(40);
						balanceLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                		
                		enterPricetextField.setEditable(true);
                		
						topGrid.add(vbButtons, 3, 0);
						
						VBox vbButtons5 = new VBox();
                		vbButtons5.setSpacing(100);
                		vbButtons5.setPadding(new Insets(330,20,20,20));
                		vbButtons5.getChildren().addAll(checkOutButton, cancelTransaction, returnItembutton);
                		topGrid.add(vbButtons5, 4, 0);
						
						VBox vbButtons2 = new VBox();
						vbButtons2.setSpacing(100);
						vbButtons2.setPadding(new Insets(330,20,20,20));
						vbButtons2.getChildren().addAll(balanceLabel, balanceAmountLabel);
               		
						topGrid.add(vbButtons2, 5, 0);
						
                		checkOutButton.setPrefHeight(40);
                		checkOutButton.setDefaultButton(true);
                		checkOutButton.setPrefWidth(100);
						
						VBox vbButtons3 = new VBox();
						
						vbButtons3.setSpacing(100);
						vbButtons3.setPadding(new Insets(247,20,20,20));
						vbButtons3.getChildren().addAll(export, cashier, register);
						
						topGrid.add(vbButtons3, 6, 0);
						export.setPrefHeight(40);
						export.setDefaultButton(true);
						export.setPrefWidth(120);
						
						cashier.setPrefHeight(40);
						cashier.setDefaultButton(true);
						cashier.setPrefWidth(100);
						
						register.setPrefHeight(40);
						register.setDefaultButton(true);
						register.setPrefWidth(120);

                    }
            		
                }
                else {
                	showAlert(Alert.AlertType.ERROR, topGrid.getScene().getWindow(), "Login Error!", "Invalid Username/Password!");
                	return;
                }
            }
        });
	    
	    createUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(nameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, topGrid.getScene().getWindow(), "Form Error!", "Please enter your name");
                    return;
                }
                if(passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, topGrid.getScene().getWindow(), "Form Error!", "Please enter a password");
                    return;
                }

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("You have been successfully registered!");
                alert.setContentText("Login using user name and password");
                
                Optional<ButtonType> result = alert.showAndWait();
      
                if (result.get() == ButtonType.OK) {
                   
                	User newuser = new User(nameField.getText(), passwordField.getText() );  
                	userList.addUser(newuser);
                	
                	nameField.clear();
                	passwordField.clear();

                	return;

                }
                
            }
        });
	   	    
		topGrid.getRowConstraints().add(new RowConstraints(100));
		topGrid.getColumnConstraints().add(new ColumnConstraints(500));
		
		//setup look and feel, fonts, alignment, etc			
		topGrid.setPrefSize(600, 600);
		root.setPrefSize(2000, 5000);
		BorderPane.setMargin(topGrid, new Insets(10, 10, 10, 40));
		
		return root;
	}

	protected void showAlert(AlertType error, Window window, String title, String message) {
	       Alert alert = new Alert(error);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.show();
		
	}
	
}
