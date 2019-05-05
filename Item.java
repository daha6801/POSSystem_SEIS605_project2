package JavaFX11;

public class Item {

	String name;
	int unitQuantity;
	double unitPrice;
	
	
	public String toString() {
		return name;
	}
	
	Item(String name, int quantity, double unitPrice) {
		this.name = name;
		this.unitQuantity = quantity;
		this.unitPrice = unitPrice;
	}
	
}
