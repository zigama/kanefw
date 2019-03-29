package com.pivotaccess.kanefw.service.dto;

import com.pivotaccess.kanefw.domain.DeviceHealth;

public class DeviceHealthDTO {
	
    private String deviceId;

    private String timeStamp;

    private String rssi;

    private String locationLat;

    private String locationLong;

    private String devicePhoneNumber;

    private String deviceCarrier;

    private String printerStatus;

    private Boolean updateAvailable;

    private Boolean updateRequired;

    private String newAppVersion;

    private String otaServerIp;

    private String newAppFileName;
    
    

	public String getDeviceId() {
		return deviceId;
	}



	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	public String getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String getRssi() {
		return rssi;
	}



	public void setRssi(String rssi) {
		this.rssi = rssi;
	}



	public String getLocationLat() {
		return locationLat;
	}



	public void setLocationLat(String locationLat) {
		this.locationLat = locationLat;
	}



	public String getLocationLong() {
		return locationLong;
	}



	public void setLocationLong(String locationLong) {
		this.locationLong = locationLong;
	}



	public String getDevicePhoneNumber() {
		return devicePhoneNumber;
	}



	public void setDevicePhoneNumber(String devicePhoneNumber) {
		this.devicePhoneNumber = devicePhoneNumber;
	}



	public String getDeviceCarrier() {
		return deviceCarrier;
	}



	public void setDeviceCarrier(String deviceCarrier) {
		this.deviceCarrier = deviceCarrier;
	}



	public String getPrinterStatus() {
		return printerStatus;
	}



	public void setPrinterStatus(String printerStatus) {
		this.printerStatus = printerStatus;
	}



	public Boolean getUpdateAvailable() {
		return updateAvailable;
	}



	public void setUpdateAvailable(Boolean updateAvailable) {
		this.updateAvailable = updateAvailable;
	}



	public Boolean getUpdateRequired() {
		return updateRequired;
	}



	public void setUpdateRequired(Boolean updateRequired) {
		this.updateRequired = updateRequired;
	}



	public String getNewAppVersion() {
		return newAppVersion;
	}



	public void setNewAppVersion(String newAppVersion) {
		this.newAppVersion = newAppVersion;
	}



	public String getOtaServerIp() {
		return otaServerIp;
	}



	public void setOtaServerIp(String otaServerIp) {
		this.otaServerIp = otaServerIp;
	}



	public String getNewAppFileName() {
		return newAppFileName;
	}



	public void setNewAppFileName(String newAppFileName) {
		this.newAppFileName = newAppFileName;
	}



	public DeviceHealthDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	public DeviceHealthDTO(DeviceHealth deviceHealth) {
		
		this.deviceId = String.valueOf(deviceHealth.getDevice().getId());
		this.timeStamp = String.valueOf(deviceHealth.getTimeStamp().getEpochSecond());
		this.rssi = deviceHealth.getRssi();
		this.locationLat = deviceHealth.getLocationLat();
		this.locationLong = deviceHealth.getLocationLong();
		this.devicePhoneNumber = deviceHealth.getDevicePhoneNumber();
		this.deviceCarrier = deviceHealth.getDeviceCarrier();
		this.printerStatus  = deviceHealth.getPrinterStatus();
		this.updateAvailable = deviceHealth.isUpdateAvailable();
		this.updateRequired = deviceHealth.isUpdateRequired();
		this.newAppVersion = deviceHealth.getNewAppVersion();
		this.otaServerIp = deviceHealth.getOtaServerIp();
		this.newAppFileName = deviceHealth.getNewAppFileName();
		
	}

}
