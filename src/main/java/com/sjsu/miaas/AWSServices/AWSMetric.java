package com.sjsu.miaas.AWSServices;

import java.math.BigDecimal;

public class AWSMetric {
	
	String instanceID = null;
	double CPUUtilization = 0;
	double DiskWriteBytes;
	BigDecimal availableResources;

	public BigDecimal getAvailableResources() {
		return availableResources;
	}

	public void setAvailableResources(BigDecimal availableResources) {
		this.availableResources = availableResources;
	}

	public AWSMetric()
	{
		
	}

	public AWSMetric(String instanceID, double CPUUtilization)
	{
		this.instanceID = instanceID;
		this.CPUUtilization = CPUUtilization;
	}
	
	
	public String getinstanceid() {
		return instanceID;
	}

	public double getCPUUtilization() {
		return CPUUtilization;
	}

	public void setinstanceid(String instance) {
		this.instanceID = instance;
	}

	public void setCPUUtilization(double CPUUtilization) {
		this.CPUUtilization = CPUUtilization;
	}
}
