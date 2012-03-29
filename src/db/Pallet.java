package db;

public class Pallet {
	public static final int UNBLOCKED = 0;
	private static final int BLOCKED = 1;
	private int pNbr;
	private String type, pDate, pTime, fDate, lDate;
	private int blocked;

	public Pallet(int pNbr, String pName, String pDate, String pTime) {
		this.pNbr = pNbr;
		this.type = pName;
		this.pDate = pDate;
		this.pTime = pTime;
		blocked = UNBLOCKED;

	}

	public boolean getBlocked() {
		return 1==blocked;
	}

	public void setBlocked(int mode) {
		blocked = mode;
	}

}
