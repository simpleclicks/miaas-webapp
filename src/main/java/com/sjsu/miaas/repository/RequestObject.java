package com.sjsu.miaas.repository;

public class RequestObject {
	String ResourceVersion = null;
	int ResourcePrice = 0;
	
	
	public RequestObject(){
		
	}

	public RequestObject(String ResourceVersion,int ResourcePrice){
		this.ResourceVersion = ResourceVersion;
		this.ResourcePrice = ResourcePrice;
		
	}
	
	public String getResourceVersion(){
		return ResourceVersion;
		
	}
	
	public double getResourcePrice(){
		return ResourcePrice;
	}
	
	public void setResourceVersion(String ResourceVersion){
		this.ResourceVersion = ResourceVersion;
	}
	
	public void setResourcePrice(int ResourcePrice){
		this.ResourcePrice = ResourcePrice;
	}
}
