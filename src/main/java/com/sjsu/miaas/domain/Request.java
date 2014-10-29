package com.sjsu.miaas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.sjsu.miaas.domain.util.CustomLocalDateSerializer;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * A Request.
 */
@Entity
@Table(name = "T_REQUEST")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "request_type")
    private String requestType;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "request_start_date", nullable = false)
    private LocalDate requestStartDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "request_end_date", nullable = false)
    private LocalDate requestEndDate;

    @Column(name = "resource_quantity")
    private Integer resourceQuantity;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "resource_version")
    private String resourceVersion;

    @Column(name = "resource_memory")
    private String resourceMemory;

    @Column(name = "resource_backup")
    private Boolean resourceBackup;

    @Column(name = "user_login", insertable=false, updatable=false)
    private String user_login;

    public String getUser_login() {
		return user_login;
	}

	public void setAmazoninstance_id(String user_login) {
		this.user_login = user_login;
	}

	@ManyToOne
    @JoinColumn(name="user_login")
    private User user;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "request_id")
    @JsonIgnore
    private Set<Device> devices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public LocalDate getRequestStartDate() {
        return requestStartDate;
    }

    public void setRequestStartDate(LocalDate requestStartDate) {
        this.requestStartDate = requestStartDate;
    }

    public LocalDate getRequestEndDate() {
        return requestEndDate;
    }

    public void setRequestEndDate(LocalDate requestEndDate) {
        this.requestEndDate = requestEndDate;
    }

    public Integer getResourceQuantity() {
        return resourceQuantity;
    }

    public void setResourceQuantity(Integer resourceQuantity) {
        this.resourceQuantity = resourceQuantity;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getResourceMemory() {
        return resourceMemory;
    }

    public void setResourceMemory(String resourceMemory) {
        this.resourceMemory = resourceMemory;
    }

    public Boolean getResourceBackup() {
        return resourceBackup;
    }

    public void setResourceBackup(Boolean resourceBackup) {
        this.resourceBackup = resourceBackup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Request request = (Request) o;

        if (id != null ? !id.equals(request.id) : request.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", requestType='" + requestType + "'" +
                ", requestStartDate='" + requestStartDate + "'" +
                ", requestEndDate='" + requestEndDate + "'" +
                ", resourceQuantity='" + resourceQuantity + "'" +
                ", resourceType='" + resourceType + "'" +
                ", resourceVersion='" + resourceVersion + "'" +
                ", resourceMemory='" + resourceMemory + "'" +
                ", resourceBackup='" + resourceBackup + "'" +
                '}';
    }
}
