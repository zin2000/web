package dto;

public class DonjonEquItem {
	private int itemDetailId;
	private int itemTypeId;
	private int itemImgId;
	private String itemName;
	private int point;
	private int skillId;
	private boolean equFlag;
	private int useCount;
	private int itemVersion;
	private String itemTypeName;
	private String itemImgBinary;
	private String itemImgMime;

    public DonjonEquItem(int itemDetailId, int itemTypeId, int itemImgId,
			String itemName, int point, int skillId, boolean equFlag,
			int useCount, int itemVersion, String itemTypeName,
			String itemImgBinary, String itemImgMime) {
		super();
		this.itemDetailId = itemDetailId;
		this.itemTypeId = itemTypeId;
		this.itemImgId = itemImgId;
		this.itemName = itemName;
		this.point = point;
		this.skillId = skillId;
		this.equFlag = equFlag;
		this.useCount = useCount;
		this.itemVersion = itemVersion;
		this.itemTypeName = itemTypeName;
		this.itemImgBinary = itemImgBinary;
		this.itemImgMime = itemImgMime;
	}

	/**
	 * @return the itemDetailId
	 */
	public int getItemDetailId() {
		return itemDetailId;
	}

	/**
	 * @param itemDetailId the itemDetailId to set
	 */
	public void setItemDetailId(int itemDetailId) {
		this.itemDetailId = itemDetailId;
	}

	/**
	 * @return the itemTypeId
	 */
	public int getItemTypeId() {
		return itemTypeId;
	}

	/**
	 * @param itemTypeId the itemTypeId to set
	 */
	public void setItemTypeId(int itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	/**
	 * @return the itemImgId
	 */
	public int getItemImgId() {
		return itemImgId;
	}

	/**
	 * @param itemImgId the itemImgId to set
	 */
	public void setItemImgId(int itemImgId) {
		this.itemImgId = itemImgId;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}

	/**
	 * @return the skillId
	 */
	public int getSkillId() {
		return skillId;
	}

	/**
	 * @param skillId the skillId to set
	 */
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	/**
	 * @return the equFlag
	 */
	public boolean isEquFlag() {
		return equFlag;
	}

	/**
	 * @param equFlag the equFlag to set
	 */
	public void setEquFlag(boolean equFlag) {
		this.equFlag = equFlag;
	}

	/**
	 * @return the useCount
	 */
	public int getUseCount() {
		return useCount;
	}

	/**
	 * @param useCount the useCount to set
	 */
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	/**
	 * @return the itemVersion
	 */
	public int getItemVersion() {
		return itemVersion;
	}

	/**
	 * @param itemVersion the itemVersion to set
	 */
	public void setItemVersion(int itemVersion) {
		this.itemVersion = itemVersion;
	}

	/**
	 * @return the itemTypeName
	 */
	public String getItemTypeName() {
		return itemTypeName;
	}

	/**
	 * @param itemTypeName the itemTypeName to set
	 */
	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	/**
	 * @return the itemImgBinary
	 */
	public String getItemImgBinary() {
		return itemImgBinary;
	}

	/**
	 * @param itemImgBinary the itemImgBinary to set
	 */
	public void setItemImgBinary(String itemImgBinary) {
		this.itemImgBinary = itemImgBinary;
	}

	/**
	 * @return the itemImgMime
	 */
	public String getItemImgMime() {
		return itemImgMime;
	}

	/**
	 * @param itemImgMime the itemImgMime to set
	 */
	public void setItemImgMime(String itemImgMime) {
		this.itemImgMime = itemImgMime;
	}

	public DonjonEquItem() {}

}
