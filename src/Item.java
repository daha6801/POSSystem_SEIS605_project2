package JavaFX11;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {

	private StringProperty name = new SimpleStringProperty();
	private IntegerProperty unitQuantity = new SimpleIntegerProperty();
	private DoubleProperty unitPrice = new SimpleDoubleProperty();


	Item() {
		this.name.set("");
		this.unitQuantity.set(0);
		this.unitPrice.set(0);
	}
	
	
	Item(String name, int quantity, double unitPrice) {
		this.name.set(name);
		this.unitQuantity.set(quantity);
		this.unitPrice.set(unitPrice);
	}
	
	public final String getName() { return name.get();}
	public final Integer getUnitQuantity() { return unitQuantity.get();}
	public final double getUnitPrice() { return unitPrice.get();}
	
	public final void setName(String name) { this.name.set(name);}
	public final void setUnitQuantity(Integer quantity) {this.unitQuantity.set(quantity);}
	public final void setUnitPrice(double unitPrice) { this.unitPrice.set(unitPrice);}
	
	
	public final StringProperty nameProperty() {return name;}
	public final IntegerProperty unitProperty() {return unitQuantity;}
	public final DoubleProperty priceProperty() {return unitPrice;}
	
	
	public String toString() {
		return name.get();
	}
}
