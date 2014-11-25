package com.sjsu.miaas.AWSServices;

import java.math.BigDecimal;

public class AWSMetric {
	
	String instanceID = null;
	double CPUUtilization = 0;
	double DiskWriteBytes;
	double NetworkIn;
	double NetworkOut;
	BigDecimal availableResources;
	double StatusCheckFailed;

	public BigDecimal getAvailableResources() {
		return availableResources;
	}

	public void setAvailableResources(BigDecimal availableResources) {
		this.availableResources = availableResources;
	}

	public AWSMetric()
	{
		
	}

	public AWSMetric(String instanceID, double CPUUtilization,double NetworkIn, double NetworkOut, double StatusCheckFailed)
	{
		this.instanceID = instanceID;
		this.CPUUtilization = CPUUtilization;
		this.NetworkIn = NetworkIn;
		this.NetworkOut = NetworkOut;
		this.StatusCheckFailed = StatusCheckFailed;
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
	
	public double getNetworkIn(){
		return NetworkIn;
	}
	
	public double getNetworkOut(){
		return NetworkOut;
	}
	
	public void setNetworkIn(double NetworkIn){
		this.NetworkIn = NetworkIn;
	}
	
	public void setNetworkOut(double NetworkOut){
		this.NetworkOut=NetworkOut;
	}
	
	public double getStatusCheckFailed(){
		return StatusCheckFailed;
	}
	
	public void setStatusCheckFailed(double StatusCheckFailed){
		this.StatusCheckFailed = StatusCheckFailed;
	}
}
