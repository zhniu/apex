/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restrequestor;

/**
 * This class holds a record of a REST request for the REST requestor plugin
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexRestRequest {
	private long   executionId;
	private String eventName;
	private Object event;
	private long   timestamp;
	
	public ApexRestRequest(long executionId, String eventName, Object event) {
		this.executionId = executionId;
		this.eventName = eventName;
		this.event = event;
	}

	public long getExecutionId() {
		return executionId;
	}

	public String getEventName() {
		return eventName;
	}

	public Object getEvent() {
		return event;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ApexRestRequest [executionId=" + executionId + ", eventName=" + eventName + ", event=" + event
				+ ", timestamp=" + timestamp + "]";
	}
}
