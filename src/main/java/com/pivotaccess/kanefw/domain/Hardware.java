package com.pivotaccess.kanefw.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Hardware.
 */
@Entity
@Table(name = "hardware")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hardware")
public class Hardware implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "model", nullable = false, unique = true)
    private String model;

    @Column(name = "serie")
    private String serie;

    @OneToMany(mappedBy = "hardware", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<HardwareFile> hardwareFiles = new HashSet<>();
    
    @OneToMany(mappedBy = "hardware", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Device> devices = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public Hardware model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerie() {
        return serie;
    }

    public Hardware serie(String serie) {
        this.serie = serie;
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Set<HardwareFile> getHardwareFiles() {
        return hardwareFiles;
    }

    public Hardware hardwareFiles(Set<HardwareFile> hardwareFiles) {
        this.hardwareFiles = hardwareFiles;
        return this;
    }

    public Hardware addHardwareFile(HardwareFile hardwareFile) {
        this.hardwareFiles.add(hardwareFile);
        hardwareFile.setHardware(this);
        return this;
    }

    public Hardware removeHardwareFile(HardwareFile hardwareFile) {
        this.hardwareFiles.remove(hardwareFile);
        hardwareFile.setHardware(null);
        return this;
    }

    public void setHardwareFiles(Set<HardwareFile> hardwareFiles) {
        this.hardwareFiles = hardwareFiles;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public Hardware devices(Set<Device> devices) {
        this.devices = devices;
        return this;
    }

    public Hardware addDevice(Device device) {
        this.devices.add(device);
        device.setHardware(this);
        return this;
    }

    public Hardware removeDevice(Device device) {
        this.devices.remove(device);
        device.setHardware(null);
        return this;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
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
        Hardware hardware = (Hardware) o;
        if (hardware.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hardware.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hardware{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", serie='" + getSerie() + "'" +
            "}";
    }
}
