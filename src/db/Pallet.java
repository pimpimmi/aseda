package db;

public class Pallet {
	public static final int UNBLOCKED = 0;
	private static final int BLOCKED = 1;
	private int id;
	private String type, pDate, pTime, fDate, lDate;
	private boolean blocked;

	public Pallet(int id, String type, String pDate, String pTime,
			String fDate, String lDate) {
		this.id = id;
		this.type = type;
		this.pDate = pDate;
		this.pTime = pTime;
		blocked = false;

	}

	public boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean mode) {
		blocked = mode;
	}

}
