package com.pivotaccess.kanefw.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DeviceHealth.
 */
@Entity
@Table(name = "device_health")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "devicehealth")
public class DeviceHealth implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_stamp")
    private Instant timeStamp;

    @Column(name = "rssi")
    private String rssi;

    @Column(name = "location_lat")
    private String locationLat;

    @Column(name = "location_long")
    private String locationLong;

    @Column(name = "device_phone_number")
    private String devicePhoneNumber;

    @Column(name = "device_carrier")
    private String deviceCarrier;

    @Column(name = "printer_status")
    private String printerStatus;

    @Column(name = "update_available")
    private Boolean updateAvailable;

    @Column(name = "update_required")
    private Boolean updateRequired;

    @Column(name = "new_app_version")
    private String newAppVersion;

    @Column(name = "ota_server_ip")
    private String otaServerIp;

    @Column(name = "new_app_file_name")
    private String newAppFileName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("deviceHealths")
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public DeviceHealth timeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRssi() {
        return rssi;
    }

    public DeviceHealth rssi(String rssi) {
        this.rssi = rssi;
        return this;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public DeviceHealth locationLat(String locationLat) {
        this.locationLat = locationLat;
        return this;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public DeviceHealth locationLong(String locationLong) {
        this.locationLong = locationLong;
        return this;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getDevicePhoneNumber() {
        return devicePhoneNumber;
    }

    public DeviceHealth devicePhoneNumber(String devicePhoneNumber) {
        this.devicePhoneNumber = devicePhoneNumber;
        return this;
    }

    public void setDevicePhoneNumber(String devicePhoneNumber) {
        this.devicePhoneNumber = devicePhoneNumber;
    }

    public String getDeviceCarrier() {
        return deviceCarrier;
    }

    public DeviceHealth deviceCarrier(String deviceCarrier) {
        this.deviceCarrier = deviceCarrier;
        return this;
    }

    public void setDeviceCarrier(String deviceCarrier) {
        this.deviceCarrier = deviceCarrier;
    }

    public String getPrinterStatus() {
        return printerStatus;
    }

    public DeviceHealth printerStatus(String printerStatus) {
        this.printerStatus = printerStatus;
        return this;
    }

    public void setPrinterStatus(String printerStatus) {
        this.printerStatus = printerStatus;
    }

    public Boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public DeviceHealth updateAvailable(Boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
        return this;
    }

    public void setUpdateAvailable(Boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
    }

    public Boolean isUpdateRequired() {
        return updateRequired;
    }

    public DeviceHealth updateRequired(Boolean updateRequired) {
        this.updateRequired = updateRequired;
        return this;
    }

    public void setUpdateRequired(Boolean updateRequired) {
        this.updateRequired = updateRequired;
    }

    public String getNewAppVersion() {
        return newAppVersion;
    }

    public DeviceHealth newAppVersion(String newAppVersion) {
        this.newAppVersion = newAppVersion;
        return this;
    }

    public void setNewAppVersion(String newAppVersion) {
        this.newAppVersion = newAppVersion;
    }

    public String getOtaServerIp() {
        return otaServerIp;
    }

    public DeviceHealth otaServerIp(String otaServerIp) {
        this.otaServerIp = otaServerIp;
        return this;
    }

    public void setOtaServerIp(String otaServerIp) {
        this.otaServerIp = otaServerIp;
    }

    public String getNewAppFileName() {
        return newAppFileName;
    }

    public DeviceHealth newAppFileName(String newAppFileName) {
        this.newAppFileName = newAppFileName;
        return this;
    }

    public void setNewAppFileName(String newAppFileName) {
        this.newAppFileName = newAppFileName;
    }

    public Device getDevice() {
        return device;
    }

    public DeviceHealth device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceHealth deviceHealth = (DeviceHealth) o;
        if (deviceHealth.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceHealth.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceHealth{" +
            "id=" + getId() +
            ", timeStamp='" + getTimeStamp() + "'" +
            ", rssi='" + getRssi() + "'" +
            ", locationLat='" + getLocationLat() + "'" +
            ", locationLong='" + getLocationLong() + "'" +
            ", devicePhoneNumber='" + getDevicePhoneNumber() + "'" +
            ", deviceCarrier='" + getDeviceCarrier() + "'" +
            ", printerStatus='" + getPrinterStatus() + "'" +
            ", updateAvailable='" + isUpdateAvailable() + "'" +
            ", updateRequired='" + isUpdateRequired() + "'" +
            ", newAppVersion='" + getNewAppVersion() + "'" +
            ", otaServerIp='" + getOtaServerIp() + "'" +
            ", newAppFileName='" + getNewAppFileName() + "'" +
            "}";
    }
}
