package db;

public class Pallet {
	private int id, type, quantity;
	private String customer, fDate, lDate;
	private boolean blocked;

	public Pallet(int id, int type, int quantity, String customer,
			String fDate, String lDate) {
		this.id = id;
		this.type = type;
		this.quantity = quantity;
		this.customer = customer;
		this.fDate = fDate;
		this.lDate = lDate;
		blocked = false;

	}
	
	public boolean getBlocked(){
		return blocked;
	}
	
	public void setBlocked(boolean mode){
		blocked = mode;
	}

}
