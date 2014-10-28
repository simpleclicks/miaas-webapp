package com.sjsu.miaas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import scala.math.BigInt;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * A Device.
 */
@Entity
@Table(name = "T_DEVICE")
public class Device implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_image_name")
    private String deviceImageName;

    @Column(name = "device_status")
    private String deviceStatus;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "device_version")
    private String deviceVersion;

    @Column(name = "device_memory")
    private String deviceMemory;
    
    @Column(name = "amazoninstance_id", insertable=false, updatable=false)
    private BigInteger amazoninstance_id;

    public BigInteger getAmazoninstance_id() {
		return amazoninstance_id;
	}

	public void setAmazoninstance_id(BigInteger amazoninstance_id) {
		this.amazoninstance_id = amazoninstance_id;
	}

	@ManyToOne
    @JoinColumn(name="amazoninstance_id")
    private AmazonInstance amazonInstance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceImageName() {
        return deviceImageName;
    }

    public void setDeviceImageName(String deviceImageName) {
        this.deviceImageName = deviceImageName;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceMemory() {
        return deviceMemory;
    }

    public void setDeviceMemory(String deviceMemory) {
        this.deviceMemory = deviceMemory;
    }

    public AmazonInstance getAmazonInstance() {
        return amazonInstance;
    }

    public void setAmazonInstance(AmazonInstance amazonInstance) {
        this.amazonInstance = amazonInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Device device = (Device) o;

        if (id != null ? !id.equals(device.id) : device.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceId='" + deviceId + "'" +
                ", deviceImageName='" + deviceImageName + "'" +
                ", deviceStatus='" + deviceStatus + "'" +
                ", deviceType='" + deviceType + "'" +
                ", deviceVersion='" + deviceVersion + "'" +
                ", deviceMemory='" + deviceMemory + "'" +
                '}';
    }
}
