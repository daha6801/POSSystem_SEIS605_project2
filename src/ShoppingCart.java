package src;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingCart {
	
	private StringProperty name = new SimpleStringProperty();
	private IntegerProperty unitQuantity = new SimpleIntegerProperty();
	private DoubleProperty totalPrice = new SimpleDoubleProperty();


	//ShoppingCart() {
	//	this.name.set("");
	//	this.unitQuantity.set(0);
	//	this.totalPrice.set(0);
	//}
	
	
	ShoppingCart(String name, int quantity, double price) {
		this.name.set(name);
		this.unitQuantity.set(quantity);
		this.totalPrice.set(price);
	}
	
	public final String getName() { return name.get();}
	public final Integer getUnitQuantity() { return unitQuantity.get();}
	public final double getTotalPrice() { return totalPrice.get();}
	
	public final void setName(String name) { this.name.set(name);}
	public final void setUnitQuantity(Integer quantity) {this.unitQuantity.set(quantity);}
	public final void setTotalPrice(double price) { this.totalPrice.set(price);}
	
	
	public final StringProperty nameProperty() {return name;}
	public final IntegerProperty unitProperty() {return unitQuantity;}
	public final DoubleProperty priceProperty() {return totalPrice;}
	
	
	public String toString() {
		return name.get();
	}

}
