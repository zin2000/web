package dto;

public class OldcoinMaster {
	private int masterId;
	private String name;
	private int startYear;
	private int endtYear;
	private String note;

	public OldcoinMaster() {}

	public OldcoinMaster(int masterId, String name, int startYear,
			int endtYear, String note) {
		super();
		this.masterId = masterId;
		this.name = name;
		this.startYear = startYear;
		this.endtYear = endtYear;
		this.note = note;
	}

	/**
	 * @return the masterId
	 */
	public int getMasterId() {
		return masterId;
	}

	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startYear
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	/**
	 * @return the endtYear
	 */
	public int getEndtYear() {
		return endtYear;
	}

	/**
	 * @param endtYear the endtYear to set
	 */
	public void setEndtYear(int endtYear) {
		this.endtYear = endtYear;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
}
