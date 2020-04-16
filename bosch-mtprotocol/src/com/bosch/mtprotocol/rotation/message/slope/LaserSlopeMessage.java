package com.bosch.mtprotocol.rotation.message.slope;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for laser slope message as 
 * described in commands 102 and 103
 * 
 * @author tos2si1
 */
public class LaserSlopeMessage implements MtMessage {
	
	public static final int SLOPE_MODE_OFF_OR_NOT_VALID = 0;
	public static final int SLOPE_MODE_ON_PR_VALID = 1;

	private int levellingMode;
	private int xValidity;
	private int yValidity;

	
	private int xData; 
	private int yData;
	/**
	 * @return the xValidity
	 */
	public int getxValidity() {
		return xValidity;
	}
	/**
	 * @param xValidity the xValidity to set
	 */
	public void setxValidity(int xValidity) {
		this.xValidity = xValidity;
	}
	/**
	 * @return the yValidity
	 */
	public int getyValidity() {
		return yValidity;
	}
	/**
	 * @param yValidity the yValidity to set
	 */
	public void setyValidity(int yValidity) {
		this.yValidity = yValidity;
	}
	/**
	 * @return the levellingMode
	 */
	public int getLevellingMode() {
		return levellingMode;
	}
	/**
	 * @param levellingMode the levellingMode to set
	 */
	public void setLevellingMode(int levellingMode) {
		this.levellingMode = levellingMode;
	}
	/**
	 * @return the xData
	 */
	public int getxData() {
		return xData;
	}
	/**
	 * @param xData the xData to set
	 */
	public void setxData(int xData) {
		this.xData = xData;
	}
	/**
	 * @return the yData
	 */
	public int getyData() {
		return yData;
	}
	/**
	 * @param yData the yData to set
	 */
	public void setyData(int yData) {
		this.yData = yData;
	}
	
	public String toString() {
		return "LaserSlopeMessage: [levellingMode=" +levellingMode + "; xValidity=" + xValidity + "; yValidity=" + yValidity
				+  "; xData=" + xData + "; yData=" + yData + "]";
	}

}
