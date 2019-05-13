package src;
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
	private IntegerProperty selectQuantity = new SimpleIntegerProperty();


	Item() {
		this.name.set("");
		this.unitQuantity.set(0);
		this.unitPrice.set(0);
		this.selectQuantity.set(0);
	}
	
	
	Item(String name, int quantity, double unitPrice, Integer selectquantity) {
		this.name.set(name);
		this.unitQuantity.set(quantity);
		this.unitPrice.set(unitPrice);
		this.selectQuantity.set(selectquantity);
	}
	
	public final String getName() { return name.get();}
	public final Integer getUnitQuantity() { return unitQuantity.get();}
	public final double getUnitPrice() { return unitPrice.get();}
	public final Integer getselectQuantity() { return selectQuantity.get();}
	
	
	public final void setName(String name) { this.name.set(name);}
	public final void setUnitQuantity(Integer quantity) {this.unitQuantity.set(quantity);}
	public final void setUnitPrice(double unitPrice) { this.unitPrice.set(unitPrice);}
	public final void setselectQuantity(int selectQuantity) { this.selectQuantity.set(selectQuantity);}
	
	
	public final StringProperty nameProperty() {return name;}
	public final IntegerProperty unitProperty() {return unitQuantity;}
	public final DoubleProperty priceProperty() {return unitPrice;}
	public final IntegerProperty selectProperty() {return selectQuantity;}
	
	public String toString() {
		return name.get();
	}
	 
	public Integer returnSeclectQuantity() {
		return selectQuantity.get();
	}
}
