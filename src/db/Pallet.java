package db;

public class Pallet {
	public static final int UNBLOCKED = 0;
	private static final int BLOCKED = 1;
	private int pNbr;
	private String type, pDate, pTime, fDate, lDate;
	private boolean blocked, delivered;

	public Pallet(int pNbr, String pName, String pDate, String pTime, boolean blocked, boolean delivered) {
		this.pNbr = pNbr;
		this.type = pName;
		this.pDate = pDate;
		this.pTime = pTime;
		this.blocked = blocked;
		this.delivered = delivered;
	}

	public boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	public String[] getStrings(){
		String[] s = new String[6];
		s[0] = String.valueOf(pNbr);
		s[1] = type;
		s[2] = pDate;
		s[3] = pTime;
		if(blocked)
			s[4] = "yes";
		else
			s[4] = "no";
		if(delivered)
			s[5] = "yes";
		else
			s[5] = "no";
		return s;
	}

}
