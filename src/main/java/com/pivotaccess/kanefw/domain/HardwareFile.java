package com.pivotaccess.kanefw.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.pivotaccess.kanefw.domain.enumeration.FileStatus;

import com.pivotaccess.kanefw.domain.enumeration.FileCategory;

/**
 * A HardwareFile.
 */
@Entity
@Table(name = "hardware_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hardwarefile")
public class HardwareFile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "jhi_size", nullable = false)
    private Long size;

    @Column(name = "mime_type")
    private String mimeType;

    @NotNull
    @Column(name = "date_uploaded", nullable = false)
    private LocalDate dateUploaded;

    @NotNull
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private FileCategory category;

    @OneToOne
    @JoinColumn(unique = true)
    private Content content;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("hardwareFiles")
    private Hardware hardware;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public HardwareFile title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSize() {
        return size;
    }

    public HardwareFile size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public HardwareFile mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public HardwareFile dateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
        return this;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Integer getVersion() {
        return version;
    }

    public HardwareFile version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public FileStatus getStatus() {
        return status;
    }

    public HardwareFile status(FileStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public FileCategory getCategory() {
        return category;
    }

    public HardwareFile category(FileCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }

    public Content getContent() {
        return content;
    }

    public HardwareFile content(Content content) {
        this.content = content;
        return this;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public HardwareFile hardware(Hardware hardware) {
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
        HardwareFile hardwareFile = (HardwareFile) o;
        if (hardwareFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hardwareFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HardwareFile{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", size=" + getSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", dateUploaded='" + getDateUploaded() + "'" +
            ", version=" + getVersion() +
            ", status='" + getStatus() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
