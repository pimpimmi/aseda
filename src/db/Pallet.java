package db;

/**
 * Holds the relevant information for a pallet.
 */
public class Pallet {
	private int pNbr;
	private String type, pDate, pTime;
	private boolean blocked, delivered;

	/**
	 * Creates a new pallet object.
	 * 
	 * @param pNbr The pallet number.
	 * @param pName The product name.
	 * @param pDate The production date.
	 * @param pTime The production time.
	 * @param blocked Whether the pallet is blocked or not.
	 * @param delivered Whether the pallet has been delivered or not.
	 */
	public Pallet(int pNbr, String pName, String pDate, String pTime, boolean blocked, boolean delivered) {
		this.pNbr = pNbr;
		this.type = pName;
		this.pDate = pDate;
		this.pTime = pTime;
		this.blocked = blocked;
		this.delivered = delivered;
	}

	/**
	 * Returns the pallet number.
	 * 
	 * @return The pallet number.
	 */
	public int getPNbr(){
		return pNbr;
	}
	
	/**
	 * Returns the pallets blocked status.
	 * 
	 * @return The pallets blocked status. 
	 */
	public boolean getBlocked() {
		return blocked;
	}

	/**
	 * Sets the blocked state.
	 * 
	 * @param blocked Wheter the pallet should be blocked or unblocked.
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	/**
	 * Returns an array with each parameter as a string.
	 * 
	 * @return An array with each parameter as a string.
	 */
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
	
	/**
	 * Returns whether the pallet has been delivered or not.
	 * 
	 * @return Whether the pallet has been delivered or not.
	 */
	public boolean getDelivered() {
		return delivered;
	}

}
