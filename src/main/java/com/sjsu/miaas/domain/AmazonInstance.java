package com.sjsu.miaas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * A AmazonInstance.
 */
@Entity
@Table(name = "T_AMAZONINSTANCE")
public class AmazonInstance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "instance_image_id")
    private String instanceImageId;

    @Column(name = "instance_type")
    private String instanceType;

    @Column(name = "instance_region")
    private String instanceRegion;

    @Column(name = "instance_status")
    private String instanceStatus;
    
    @Column(name = "available_resources")
    private BigDecimal availableResources;

    public BigDecimal getAvailableResources() {
		return availableResources;
	}

	public void setAvailableResources(BigDecimal availableResources) {
		this.availableResources = availableResources;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "amazoninstance_id")
    @JsonIgnore
    private Set<Device> devices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceImageId() {
        return instanceImageId;
    }

    public void setInstanceImageId(String instanceImageId) {
        this.instanceImageId = instanceImageId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getInstanceRegion() {
        return instanceRegion;
    }

    public void setInstanceRegion(String instanceRegion) {
        this.instanceRegion = instanceRegion;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AmazonInstance amazoninstance = (AmazonInstance) o;

        if (id != null ? !id.equals(amazoninstance.id) : amazoninstance.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AmazonInstance{" +
                "id=" + id +
                ", instanceId='" + instanceId + "'" +
                ", instanceImageId='" + instanceImageId + "'" +
                ", instanceType='" + instanceType + "'" +
                ", instanceRegion='" + instanceRegion + "'" +
                ", instanceStatus='" + instanceStatus + "'" +
                '}';
    }
}
