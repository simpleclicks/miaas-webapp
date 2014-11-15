package com.sjsu.miaas.AWSServices;

public class AWSMetric {
	
	String instanceID = null;
	double CPUUtilization = 0;
	double DiskWriteBytes;

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
