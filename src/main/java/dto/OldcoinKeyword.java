package dto;

public class OldcoinKeyword {
	private int keyId;
	private String keyName;
	private String keyKana;
	private String keyNote;

	public OldcoinKeyword() {}

	public OldcoinKeyword(int keyId, String keyName, String keyKana,
			String keyNote) {
		super();
		this.keyId = keyId;
		this.keyName = keyName;
		this.keyKana = keyKana;
		this.keyNote = keyNote;
	}

	/**
	 * @return the keyId
	 */
	public int getKeyId() {
		return keyId;
	}

	/**
	 * @param keyId the keyId to set
	 */
	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	/**
	 * @return the keyName
	 */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * @param keyName the keyName to set
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * @return the keyKana
	 */
	public String getKeyKana() {
		return keyKana;
	}

	/**
	 * @param keyKana the keyKana to set
	 */
	public void setKeyKana(String keyKana) {
		this.keyKana = keyKana;
	}

	/**
	 * @return the keyNote
	 */
	public String getKeyNote() {
		return keyNote;
	}

	/**
	 * @param keyNote the keyNote to set
	 */
	public void setKeyNote(String keyNote) {
		this.keyNote = keyNote;
	}
	
}
