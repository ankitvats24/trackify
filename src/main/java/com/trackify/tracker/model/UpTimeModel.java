package com.trackify.tracker.model;

import java.sql.Date;
import java.sql.Timestamp;

public class UpTimeModel {

	private String deviceName;
	private String deviceId;
	private boolean status = false;
	private String logTime;
	private int upTime;
	private int downTime;
	private Date createdDate;
	private Timestamp statusLastUpdated;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	@Override
	public String toString() {
		return "UpTimeModel [deviceName=" + deviceName + ", deviceId=" + deviceId + ", status=" + status + ", logTime="
				+ logTime + ", upTime=" + upTime + ", downTime=" + downTime + "]";
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getUpTime() {
		return upTime;
	}
	public void setUpTime(int upTime) {
		this.upTime = upTime;
	}
	public int getDownTime() {
		return downTime;
	}
	public void setDownTime(int downTime) {
		this.downTime = downTime;
	}
	public Timestamp getStatusLastUpdated() {
		return statusLastUpdated;
	}
	public void setStatusLastUpdated(Timestamp statusLastUpdated) {
		this.statusLastUpdated = statusLastUpdated;
	}
}
