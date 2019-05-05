package JavaFX11;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Point_of_sale_system extends Application {
	Model model = new Model();
	View view = new View();
	private Text actionStatus;
	
	public static void main(String[] args) {
		launch(args);
	}

	 public void start(Stage primaryStage) throws Exception {

		 Scene scene = new Scene(view.setupScene(), 800, 700);
		 
			primaryStage.setScene(scene);
			primaryStage.show();
	
		
			model.loadData(); //this will load the data from .csv file
	        //Scene scene = new Scene(new Group());
	        primaryStage.setTitle("Point Of Sale System");
	        primaryStage.setWidth(1000);
	        primaryStage.setHeight(600);

	        final Label label = new Label("Welcome to Point of Sale System");
	        
	        view.tableView.setEditable(true);
	        view.tableView.getSelectionModel().setCellSelectionEnabled(true);
	        
	        
	        ObservableList<TableColumn> tableViewColumns = generateTableViewColumns();
	        view.tableView.getColumns().setAll(tableViewColumns);

	        ObservableList<TableColumn> tcs = view.tableView.getColumns();
	        for (int i = 0; i < tcs.size(); i++) {
	            TableColumn tc = tcs.get(i);

	            Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>> cellValueFactory = buildCallbackString(i);
	            tc.setCellValueFactory(cellValueFactory);

	        }

	        ObservableList<ArrayList> tableViewRows = generateTableViewRows();
	        view.tableView.getItems().setAll(tableViewRows);

	        for (int i = 0; i < tcs.size(); i++) {
	            TableColumn dataColumn = tcs.get(i);

	             Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>> cellFactoryTextFieldTableCell = buildCallbackTextFieldTableCell();
	             dataColumn.setCellFactory(cellFactoryTextFieldTableCell);

	        }

	        final VBox vbox = new VBox();

	        vbox.setSpacing(5);
	        vbox.setPadding(new Insets(10, 0, 0, 10));
	        vbox.getChildren().addAll(label, view.tableView);

	        //((Group) scene.getRoot()).getChildren().addAll(vbox);

	        primaryStage.setScene(scene);

	        primaryStage.show(); 
	    }


	    private ObservableList<TableColumn> generateTableViewColumns() {
	        ObservableList<TableColumn> tableViewColumns = FXCollections.observableArrayList();
	        TableColumn firstDataColumn = new TableColumn<>("Item");
	        TableColumn secondDataColumn = new TableColumn<>("Quantity");
	        TableColumn thirdDataColumn = new TableColumn<>("Price");
	        //TableColumn fourthDataColumn = new TableColumn<>("Select");

	        firstDataColumn.setMinWidth(200);
	        secondDataColumn.setMinWidth(200);
	        thirdDataColumn.setMinWidth(200);
	        //fourthDataColumn.setMinWidth(200);

	        tableViewColumns.add(firstDataColumn);
	        tableViewColumns.add(secondDataColumn);
	        tableViewColumns.add(thirdDataColumn); 
	        //tableViewColumns.add(fourthDataColumn);

	        return tableViewColumns;
	    }

	    private ObservableList<ArrayList> generateTableViewRows() {

	        ObservableList<ArrayList> tableViewRows = FXCollections.observableArrayList();
	        for (int i = 0; i < model.itemsObservableList.size() ; i++) {
	            ArrayList dataRow = new ArrayList<>();

	            String value1 = model.itemsObservableList.get(i).name;
	            String  value2 = Double.toString(model.itemsObservableList.get(i).unitQuantity);
	            String  value3 = Double.toString(model.itemsObservableList.get(i).unitPrice);
	            //String value4 = Double.toString(model.itemsObservableList.get(i).unitPrice);
	            
	            Spinner<Integer> selectSpinner = new Spinner<>();
	            //IntegerSpinnerValueFactory spinnerFactorySelect = new IntegerSpinnerValueFactory(0,84,36);
	            //selectSpinner.setValueFactory(spinnerFactorySelect);	            
	            
	            dataRow.add(value1);
	            dataRow.add(value2);
	            dataRow.add(value3);
	            //dataRow.add(selectSpinner);

	            tableViewRows.add(dataRow);

	        }
	        return tableViewRows;
	    }

	    private Callback<TableColumn.CellDataFeatures<ArrayList, Color>, ObservableValue<Color>> buildCallbackColor(int index) {
	        Callback<TableColumn.CellDataFeatures<ArrayList, Color>, ObservableValue<Color>> cellValueFactory = new Callback<TableColumn.CellDataFeatures<ArrayList, Color>, ObservableValue<Color>>() {
	            @Override
	            public ObservableValue<Color> call(TableColumn.CellDataFeatures<ArrayList, Color> param) {
	                return new SimpleObjectProperty(param.getValue().get(index));
	            }
	        };
	        return cellValueFactory;
	    }

	    private Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>> buildCallbackString(int index) {
	        Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>> cellValueFactory = new Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>>() {
	            @Override
	            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList, String> param) {
	                return new SimpleStringProperty((String) param.getValue().get(index));
	            }
	        };
	        return cellValueFactory;
	    }

	    private Callback<TableColumn<ArrayList, Color>, TableCell<ArrayList, Color>> buildCallbackPane() {
	        Callback<TableColumn<ArrayList, Color>, TableCell<ArrayList, Color>> cellFactory = new Callback<TableColumn<ArrayList, Color>, TableCell<ArrayList, Color>>() {
	            @Override
	            public TableCell call(TableColumn tableColumn) {
	                double cellWidth = tableColumn.getMinWidth();
	                double cellHeight = 35;

	                TableCell tableCell = new TableCell<Object, Color>() {
	                    @Override
	                    protected void updateItem(Color item, boolean empty) {
	                        super.updateItem(item, empty);

	                        if (empty) {
	                            setText(null);
	                            setGraphic(null);
	                        } else {
	                            Pane p = new Pane();
	                            p.setPrefSize(cellWidth, cellHeight);
	                            Canvas canvasRectLayerColor = new Canvas();
	                            p.getChildren().add(canvasRectLayerColor);
	                            canvasRectLayerColor.setWidth(20);
	                            canvasRectLayerColor.setHeight(20);
	                            GraphicsContext gc = canvasRectLayerColor.getGraphicsContext2D();      
	                            gc.setFill(item);
	                            gc.fillRect(0, 0, canvasRectLayerColor.getWidth(), canvasRectLayerColor.getHeight());
	                            setGraphic(p);
	                        }
	                    }
	                };

	                return tableCell;
	            }
	        };

	        return cellFactory;
	    }

	    private Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>> buildCallbackTextFieldTableCell() {
	        Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>> cellFactory = new Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>>() {
	            @Override
	            public TableCell call(TableColumn tc) {
	                TextFieldTableCell tftc = new TextFieldTableCell();

	                return tftc;
	            }
	        };
	        return cellFactory;
	    }
	   
}