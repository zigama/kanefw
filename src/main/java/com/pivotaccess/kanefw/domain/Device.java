package com.pivotaccess.kanefw.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<DeviceHealth> deviceHealths = new HashSet<>();
    
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Transaction> transactions = new HashSet<>();
    
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("devices")
    private Hardware hardware;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Device serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Set<DeviceHealth> getDeviceHealths() {
        return deviceHealths;
    }

    public Device deviceHealths(Set<DeviceHealth> deviceHealths) {
        this.deviceHealths = deviceHealths;
        return this;
    }

    public Device addDeviceHealth(DeviceHealth deviceHealth) {
        this.deviceHealths.add(deviceHealth);
        deviceHealth.setDevice(this);
        return this;
    }

    public Device removeDeviceHealth(DeviceHealth deviceHealth) {
        this.deviceHealths.remove(deviceHealth);
        deviceHealth.setDevice(null);
        return this;
    }

    public void setDeviceHealths(Set<DeviceHealth> deviceHealths) {
        this.deviceHealths = deviceHealths;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Device transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Device addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setDevice(this);
        return this;
    }

    public Device removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setDevice(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public Device hardware(Hardware hardware) {
        this.hardware = hardware;
        return this;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
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
        Device device = (Device) o;
        if (device.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            "}";
    }
}
