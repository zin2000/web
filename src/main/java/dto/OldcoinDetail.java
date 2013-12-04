package dto;

import java.util.Date;

public class OldcoinDetail {
	private int detailId;
	private Date addDate;
	private String name;
	private String frontImgUrl;
	private String backImgUrl;
	private String fontName;
	private String materialName;
	private int startYear;
	private int endtYear;


	public OldcoinDetail(int detailId, Date addDate, String name,
			String frontImgUrl, String backImgUrl, String fontName,
			String materialName, int startYear, int endtYear) {
		super();
		this.detailId = detailId;
		this.addDate = addDate;
		this.name = name;
		this.frontImgUrl = frontImgUrl;
		this.backImgUrl = backImgUrl;
		this.fontName = fontName;
		this.materialName = materialName;
		this.startYear = startYear;
		this.endtYear = endtYear;
	}


	/**
	 * @return the detailId
	 */
	public int getDetailId() {
		return detailId;
	}


	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}


	/**
	 * @return the addDate
	 */
	public Date getAddDate() {
		return addDate;
	}


	/**
	 * @param addDate the addDate to set
	 */
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
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
	 * @return the frontImgUrl
	 */
	public String getFrontImgUrl() {
		return frontImgUrl;
	}


	/**
	 * @param frontImgUrl the frontImgUrl to set
	 */
	public void setFrontImgUrl(String frontImgUrl) {
		this.frontImgUrl = frontImgUrl;
	}


	/**
	 * @return the backImgUrl
	 */
	public String getBackImgUrl() {
		return backImgUrl;
	}


	/**
	 * @param backImgUrl the backImgUrl to set
	 */
	public void setBackImgUrl(String backImgUrl) {
		this.backImgUrl = backImgUrl;
	}


	/**
	 * @return the fontName
	 */
	public String getFontName() {
		return fontName;
	}


	/**
	 * @param fontName the fontName to set
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}


	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}


	/**
	 * @param materialName the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
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


	public OldcoinDetail() {}

}
