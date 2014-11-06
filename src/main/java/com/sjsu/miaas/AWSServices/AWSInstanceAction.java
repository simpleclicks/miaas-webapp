package com.sjsu.miaas.AWSServices;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
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

	private final static Logger log = Logger.getLogger(AWSInstanceAction.class.getName());
	
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

	public AmazonInstance CreateInstance() throws InterruptedException {
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
		AmazonInstance newDbObj = new AmazonInstance();
		newDbObj.setInstanceId(newInst.getInstanceId());
		newDbObj.setInstanceImageId(newInst.getImageId());
		newDbObj.setInstanceRegion(newInst.getPlacement().getAvailabilityZone());
		newDbObj.setInstanceStatus(newInst.getState().getName());
		newDbObj.setInstanceType(newInst.getInstanceType());
		newDbObj.setAvailableResources(new BigDecimal(10));
		//amazonInstRepo.save(newDbObj);
		return newDbObj;
	}

	public List<Double> monitorInstance(String instanceId) {
		   try {
			   AWSInstanceState awi = new AWSInstanceState();
			   BasicAWSCredentials bas = awi.getCredentials();
		       AmazonCloudWatchClient cw = new AmazonCloudWatchClient(bas) ;
		       cw.setRegion(Region.getRegion(Regions.US_WEST_2));
		       long offsetInMilliseconds = 1000 * 60 * 60 * 24;
		       GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
		               .withStartTime(new Date(new Date().getTime() - offsetInMilliseconds))
		               .withNamespace("AWS/EC2")
		               .withPeriod(60 * 60)
		               .withDimensions(new Dimension().withName("InstanceId").withValue(instanceId))
		               .withMetricName("CPUUtilization")
		               .withStatistics("Average", "Maximum")
		               .withEndTime(new Date());
		       GetMetricStatisticsResult getMetricStatisticsResult = cw.getMetricStatistics(request);
		       System.out.println(getMetricStatisticsResult.toString());
		       double avgCPUUtilization = 0;
		       List dataPoint = getMetricStatisticsResult.getDatapoints();
		       List<Double> avgcpulist = new ArrayList<Double>();
		       
		       for (Object aDataPoint : dataPoint) {
		           Datapoint dp = (Datapoint) aDataPoint;
		           avgCPUUtilization = dp.getAverage();
		           avgcpulist.add(avgCPUUtilization);
		           System.out.println(instanceId + " instance's average CPU utilization : " + dp.getAverage());
		       }

		       return avgcpulist;

		   } catch (AmazonServiceException ase) {
		       log.severe("Caught an AmazonServiceException, which means the request was made  "
		               + "to Amazon EC2, but was rejected with an error response for some reason.");
		       log.severe("Error Message:    " + ase.getMessage());
		       log.severe("HTTP Status Code: " + ase.getStatusCode());
		       log.severe("AWS Error Code:   " + ase.getErrorCode());
		       log.severe("Error Type:       " + ase.getErrorType());
		       log.severe("Request ID:       " + ase.getRequestId());

		   }
		return null;
		   
		}
}
