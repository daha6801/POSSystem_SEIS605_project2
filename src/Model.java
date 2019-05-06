package JavaFX11;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	
	ObservableList<Item> itemsObservableList = FXCollections.observableArrayList();
	
	void loadData() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader (new FileReader("C:\\Users\\rashmi.dahal1\\grad_school_java_projects\\eclipse\\POSSystem\\JavaFX11\\ItemsList.csv"))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Item info = new Item(values[0], Integer.parseInt(values[1]), Double.parseDouble(values[2]));
				itemsObservableList.add(info);
			}
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}
}