package com.sjsu.miaas.AWSServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.repository.AmazonInstanceRepository;

public class AWSInstanceAction extends AWSInstanceState {

	AmazonInstance ama = new AmazonInstance();

	@Inject
	AmazonInstanceRepository amazonInstRepo;

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

	public boolean stopInstance(String instanceId) throws Exception {

		boolean bRet = false;
		// Code to stop instance

		ArrayList<String> idr = new ArrayList<String>();
		AmazonInstance awl = new AmazonInstance();

		awl = amazonInstRepo.getAmazonInstancebyId(instanceId);

		int i;
		idr.add(instanceId);
		/*
		 * for (i = 0; i < instanceId.size(); i++) { if
		 * (instanceId.get(i).getState().toString().contains("running")) {
		 * idr.add(instanceId.get(i).getInstanceId());
		 * 
		 * } }
		 */
		if (idr.size() >= 1) {
			StopInstancesRequest startir = new StopInstancesRequest();
			startir.setInstanceIds(idr);
			// amazonEC2.stopInstances(startir);
			awl.setInstanceStatus("stopped");
			amazonInstRepo.save(awl);
			bRet = true;

		}

		return bRet;
	}

	public Instance CreateInstance() throws InterruptedException {
		Instance aws = new Instance();
		RunInstancesRequest rir = new RunInstancesRequest()
				.withInstanceType("g2.2xlarge").withKeyName("miaas")
				.withImageId("ami-213d7511").withMinCount(1).withMaxCount(1)
				.withSecurityGroupIds("sg-e7bad882");
		RunInstancesResult run = amazonEC2.runInstances(rir);
		String newInstance = run.getReservation().getInstances().get(0).getInstanceId();
		System.out.println(newInstance);
		InstanceState is = run.getReservation().getInstances().get(0).getState();
		System.out.println(is.toString());
		Instance newInst = null;
		while(!is.toString().contains("running")){
			DescribeInstancesResult dir = amazonEC2.describeInstances();
			List<Reservation> reservations = dir.getReservations();
			Set<Instance> instances = new HashSet<Instance>();
			for (Reservation reservation : reservations){
				instances.addAll(reservation.getInstances());
				
			}
			ArrayList<Instance> idr = new ArrayList<Instance>();
			
			for(Instance ins : instances){
				if(ins.getInstanceId().contains(newInstance)){
					newInst = ins;
					break;
				}			
			}
			is = newInst.getState();
			System.out.println("Entered:");
			Thread.sleep(15000);
			is = newInst.getState();
			System.out.println(is.toString());
		}
		//put a thread which checks that the state of the instance is entered running.
		//until that you keep on checking.
		// once done, describe instances and return the instance with the specified instance id 
		// in newInstance.
		
		return newInst;
	}

}