package com.sjsu.miaas.AWSServices;

import java.util.ArrayList;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;

public class AWSInstanceAction extends AWSInstanceState {

	public AWSInstanceAction() {
		super();

	}

	public boolean startInstance(ArrayList<Instance> instanceId)
			throws Exception {
		boolean bRet = false;
		

		// Code to start instance
		ArrayList<String> idr = new ArrayList<String>();
		int i;

		for (i = 0; i < instanceId.size(); i++) {
			if (instanceId.get(i).getState().toString().contains("stopped")) {
				idr.add(instanceId.get(i).getInstanceId());
			}
		}
		if (idr.size() >= 1) {
			StartInstancesRequest startir = new StartInstancesRequest();
			startir.setInstanceIds(idr);
			amazonEC2.startInstances(startir);
			bRet = true;

		}

		return bRet;
	}

	
	public boolean stopInstance(ArrayList<Instance> instanceId)
			throws Exception {

		boolean bRet = false;
		// Code to stop instance

		ArrayList<String> idr = new ArrayList<String>();

		int i;

		for (i = 0; i < instanceId.size(); i++) {
			if (instanceId.get(i).getState().toString().contains("running")) {
				idr.add(instanceId.get(i).getInstanceId());

			}
		}
		if (idr.size() >= 1) {
			StopInstancesRequest startir = new StopInstancesRequest();
			startir.setInstanceIds(idr);
			amazonEC2.stopInstances(startir);
			bRet = true;

		}

		return bRet;
	}
}
