/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.sync.list;

import java.util.ArrayList;
import java.util.List;

import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.message.sync.SyncInputMessage;




/**
 * @author AndrejsC
 *
 */
public class SyncListInputMessage implements MtMessage {

	private int indexFrom;
	private int indexTo;
	
	private List<SyncInputMessage> syncContainers = new ArrayList<SyncInputMessage>();
	
	/**
	 * @return the indexFrom
	 */
	public int getIndexFrom() {
		return indexFrom;
	}
	/**
	 * @param indexFrom the indexFrom to set
	 */
	public void setIndexFrom(int indexFrom) {
		this.indexFrom = indexFrom;
	}
	/**
	 * @return the indexTo
	 */
	public int getIndexTo() {
		return indexTo;
	}
	/**
	 * @param indexTo the indexTo to set
	 */
	public void setIndexTo(int indexTo) {
		this.indexTo = indexTo;
	}
	/**
	 * @return the syncContainers
	 */
	public List<SyncInputMessage> getSyncContainers() {
		return syncContainers;
	}
	/**
	 * @param syncContainers the syncContainers to set
	 */
	public void setSyncContainers(List<SyncInputMessage> syncContainers) {
		this.syncContainers = syncContainers;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SyncListInputMessage [indexFrom=" + indexFrom + ", indexTo="
				+ indexTo + ", syncContainers=" + syncContainers + "]";
	}
}
