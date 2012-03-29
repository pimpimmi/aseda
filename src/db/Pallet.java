package db;

public class Pallet {
	private int id;
	private String type, pDate, pTime, fDate, lDate;
	private boolean blocked;

	public Pallet(int id, String type, String pDate, String pTime, String fDate, String lDate) {
		this.id = id;
		this.type = type;
		this.pDate = pDate;
		this.pTime = pTime;
		this.fDate = fDate;
		this.lDate = lDate;
		blocked = false;

	}

	public boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean mode) {
		blocked = mode;
	}

}
