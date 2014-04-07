package dto;

public class OldcoinCharacter {
	private int characterId;
	private String characterName;

	public OldcoinCharacter() {}

	public OldcoinCharacter(int characterId, String characterName) {
		super();
		this.characterId = characterId;
		this.characterName = characterName;
	}

	/**
	 * @return the characterId
	 */
	public int getCharacterId() {
		return characterId;
	}

	/**
	 * @param characterId the characterId to set
	 */
	public void setCharacterId(int characterId) {
		this.characterId = characterId;
	}

	/**
	 * @return the characterName
	 */
	public String getCharacterName() {
		return characterName;
	}

	/**
	 * @param characterName the characterName to set
	 */
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}
	
}
