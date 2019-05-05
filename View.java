package JavaFX11;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	
	Label title = new Label("Point of Sale System");
	Label nameLabel = new Label("User Id : ");
	TextField nameField = new TextField();

	
	Label passwordLabel = new Label("Password : ");
	PasswordField passwordField = new PasswordField();
	
	
	Button submitButton = new Button("Submit");
	
	TableView tableView = new TableView<>();
	
	BorderPane setupScene() {
		
		BorderPane root = new BorderPane();
		GridPane topGrid = new GridPane();
		root.setTop(topGrid);
		
        
		/**setup TopGrid*/
		topGrid.setVgap(10);
		topGrid.setHgap(10);
		
		//add controls to topGrid	
		
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    topGrid.add(title, 0,0,2,1);
	    GridPane.setHalignment(title, HPos.CENTER);
	    GridPane.setMargin(title, new Insets(20, 0,20,0));
	    
	    
		topGrid.add(nameLabel, 0, 1);
		nameField.setPrefHeight(40);
		topGrid.add(nameField, 1,1);
	    
	    
		topGrid.add(passwordLabel, 0, 3);
	    passwordField.setPrefHeight(40);
	    topGrid.add(passwordField, 1, 3);
	    
	    
		topGrid.add(submitButton, 0, 4, 2, 1);
		
					
	    submitButton.setPrefHeight(40);
	    submitButton.setDefaultButton(true);
	    submitButton.setPrefWidth(100);
		//topGrid.add(tableView, 0, 5);
	    
	    topGrid.setHalignment(submitButton, HPos.CENTER);
	    topGrid.setMargin(submitButton, new Insets(20, 0,20,0));
		
	    submitButton.setOnAction(new EventHandler<ActionEvent>() {
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
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Welcome to the POS System");
                
                Optional<ButtonType> result = alert.showAndWait();
                
                if (result.get() == ButtonType.OK) {

            		GridPane topGrid = new GridPane();
            		root.setTop(topGrid);
            		
            		topGrid.add(tableView, 0, 5);

                }
                
            }
        });
	    
	    
		topGrid.getRowConstraints().add(new RowConstraints(100));
		topGrid.getColumnConstraints().add(new ColumnConstraints(500));
		
		//setup look and feel, fonts, alignment, etc
			
		topGrid.setPrefSize(2000, 5000);
		root.setPrefSize(2000, 5000);
		BorderPane.setMargin(topGrid, new Insets(10, 10, 10, 10));
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
