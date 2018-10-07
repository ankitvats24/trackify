package com.trackify.tracker.model;

public class UpTimeModel {

	private String deviceName;
	private String deviceId;
	private boolean status = false;
	private String logTime;
	private double upTime;
	private double downTime;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public double getUpTime() {
		return upTime;
	}
	public void setUpTime(double upTime) {
		this.upTime = upTime;
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
	public double getDownTime() {
		return downTime;
	}
	public void setDownTime(double downTime) {
		this.downTime = downTime;
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
}
